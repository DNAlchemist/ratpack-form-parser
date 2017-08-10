/*
 * MIT License
 *
 * Copyright (c) 2017 Mikhalev Ruslan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package one.chest.ratpack.form

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.Memoized
import groovy.util.logging.Slf4j
import org.codehaus.groovy.runtime.DefaultGroovyMethods
import org.codehaus.groovy.runtime.InvokerHelper
import ratpack.form.Form

import javax.validation.ValidationException
import javax.validation.ValidatorFactory
import java.lang.reflect.Field

import static javax.validation.Validation.buildDefaultValidatorFactory

@Slf4j
@CompileStatic
public final class FormParser {

    private Map<String, Field> declaredFields
    private Map<String, String> incomingValues
    Map<String, String> unusedFields

    private FormParser(Form self, Class<?> clazz) {
        declaredFields = getDeclaredFields(clazz)
        def ignorePropertiesAnnotation = clazz.getDeclaredAnnotation(FormIgnoreProperties)
        def ignoredProperties = ignorePropertiesAnnotation?.value() as List ?: []
        incomingValues = self.findAll({ !ignoredProperties.contains(it.key) })
        unusedFields = ignorePropertiesAnnotation?.ignoreUnknown() ? [:] : new HashMap<>(incomingValues)
    }

    public static <T> T parseAsType(Form self, Class<T> clazz) {
        if(isDefaultCastType(self, clazz)) {
            return DefaultGroovyMethods.asType(self, clazz)
        }

        FormParser parser = new FormParser(self, clazz)
        T instance = parser.incomingValues.inject(clazz.newInstance(), { instance, String name, value ->

            value = value ?: null

            Field field = parser.declaredFields[name]
            if(field){
                setValueToObjectProperty(instance, field.name, field.type, value)
                parser.unusedFields.remove(name)
            }
            return instance
        })
        if(!parser.unusedFields.isEmpty()) {
            throw new UnrecognizedFormPropertyException(parser.incomingValues.entrySet()[0].key, clazz)
        }

        validatorFactory?.with {
            validator.validate(instance).each({ throw new ValidationException(it.message) })
        }

        return instance
    }

    private static boolean isDefaultCastType(Form form, Class<?> clazz) {
        return Map.isAssignableFrom(clazz)
    }

    @CompileDynamic
    private static castToType(Object o, Class<?> clazz) {
        return o.asType(clazz)
    }

    @Memoized
    private static Map<String, Field> getDeclaredFields(Class<?> clazz) {
        return clazz.declaredFields.collectEntries({ field ->
            FormProperty annotation = field.getDeclaredAnnotation(FormProperty)
            String fieldName = getFormFieldName(annotation, field.name)
            return [(fieldName): field]
        })
    }

    @Memoized
    private static ValidatorFactory getValidatorFactory() {
        try {
            return buildDefaultValidatorFactory()
        } catch (ValidationException e) {
            log.warn e.message
            return null
        }
    }

    private static setValueToObjectProperty(Object instance, String propertyName, Class propertyType, Object value) {
        try {
            if (!(Number.isAssignableFrom(propertyType) || propertyType.isPrimitive()))
                return InvokerHelper.setProperty(instance, propertyName, value)
        } catch (ClassCastException ignored) {
        }
        return InvokerHelper.setProperty(instance, propertyName, castToType(value, propertyType))
    }

    private static String getFormFieldName(FormProperty formProperty, String fieldNameAsDefault) {
        return formProperty && formProperty.value() ? formProperty.value() : fieldNameAsDefault
    }

}
