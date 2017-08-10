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
package one.chest.ratpack.groovy.extension

import groovy.transform.CompileStatic
import one.chest.ratpack.form.FormProperty
import one.chest.ratpack.form.UnrecognizedFormPropertyException
import org.junit.Test
import ratpack.form.internal.DefaultForm
import ratpack.util.internal.ImmutableDelegatingMultiValueMap

import javax.validation.ValidationException
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import java.util.concurrent.atomic.AtomicInteger

@CompileStatic
public class RatpackExtensionMethodsTest {

    final ImmutableDelegatingMultiValueMap EMPTY_MAP = [:] as ImmutableDelegatingMultiValueMap


    private static class TestPOGO {
        String string
        int integer
    }

    @Test
    public void testParsing() {
        def input = [string: ['hello'], integer: ['10']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestPOGO
        assert pogo.string == "hello"
        assert pogo.integer == 10
    }

    @Test(expected = NumberFormatException)
    public void testParsingInvalidType() {
        def input = [string: ['hello'], integer: ['1.1']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestPOGO
        assert pogo.string == "hello"
        assert pogo.integer == 10
    }


    private static class TestPOGOWithNotNullValidation {
        String optional

        @NotNull
        String required
    }

    @Test(expected = ValidationException.class)
    public void testNotNullValidation() {
        def input = [optional: ['hello']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        form as TestPOGOWithNotNullValidation
    }


    private static class TestPOGOWithValidationPrimitive {
        @Min(1L)
        int positive
    }

    @Test(expected = ValidationException.class)
    public void testValidationPrimitive() {
        def input = [positive: ["0"]] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        form as TestPOGOWithValidationPrimitive
    }


    private static class TestPOGOWithSetters {
        List<String> list

        void setList(String commaSeparatedList) {
            list = commaSeparatedList.split(",")*.trim()
        }
    }

    @Test
    public void testParsingWithSetters() {
        def input = [list: ['a, b, c, d']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestPOGOWithSetters
        assert pogo.list == ['a', 'b', 'c', 'd']
    }


    private static class TestPOGOCustomFieldName {
        String formField

        @FormProperty("formField")
        String javaField
    }

    @Test
    public void testParsingWithCustomField() {
        def input = [formField: ['hello']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestPOGOCustomFieldName
        assert pogo.javaField == 'hello'
    }


    @Test
    public void testParsingJavaPOJO() {
        def input = [string: ['hello'], integer: ['10'], field: ['field']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pojo = form as TestJavaPOJO
        assert pojo.string == 'hello'
        assert pojo.integer == 10
        assert pojo.field == 'field'
    }


    @Test(expected = UnrecognizedFormPropertyException)
    public void testPropertyNotFound() {
        def input = [nonexistsproperty: ['hello']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        form as TestJavaPOJO
    }


    @Test
    public void testEmpty() {
        def input = [integer: ['']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pojo = form as TestJavaPOJO
        assert pojo.integer == null
    }


    @Test
    public void testAsHashMap() {
        def input = [integer: ['']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def hashMap = form as HashMap
        assert hashMap.integer == ""
    }


    @Test
    public void testParseJavaObject() {
        def input = [value: ['-42']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def atomicInteger = form as AtomicInteger
        assert atomicInteger.get().is(-42)
    }
}