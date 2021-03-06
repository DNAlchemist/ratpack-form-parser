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
import one.chest.ratpack.form.TypeCastException
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import ratpack.form.internal.DefaultForm
import ratpack.util.internal.ImmutableDelegatingMultiValueMap

import static java.util.Collections.emptyMap

@CompileStatic
public class TypeConversionTest {

    final ImmutableDelegatingMultiValueMap EMPTY_MAP = new ImmutableDelegatingMultiValueMap(emptyMap())

    @Rule
    public ExpectedException expectedException = ExpectedException.none()

    private static class TestInteger {
        Integer formProperty
    }

    @Test
    public void testFloatToInteger() {
        def input = [formProperty: ['1.1']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)
        expectedException.expect(TypeCastException)
        expectedException.expectMessage(/Cant't cast "1.1" to class java.lang.Integer/)
        form as TestInteger
    }

}
