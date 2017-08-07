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
package ratpack.groovy.extension

import groovy.transform.CompileStatic
import org.junit.Test
import ratpack.form.FormIgnoreProperties
import ratpack.form.internal.DefaultForm
import ratpack.util.internal.ImmutableDelegatingMultiValueMap

@CompileStatic
class FormIgnorePropertiesTest {

    final ImmutableDelegatingMultiValueMap EMPTY_MAP = [:] as ImmutableDelegatingMultiValueMap


    @FormIgnoreProperties(ignoreUnknown = true)
    private static class TestPOGOIgnoreUnknownProperties {
        String string
        int integer
    }

    @Test
    public void testParsingJavaIgnoreUnknownProperties() {
        def input = [nonexistsproperty: ['hello'], integer: ['10']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pojo = form as TestPOGOIgnoreUnknownProperties
        assert !pojo.string
        assert pojo.integer == 10
    }


    @FormIgnoreProperties("string")
    private static class TestPOGOIgnoreProperty {
        String string
        int integer
    }

    @Test
    public void testParsingJavaIgnoreProperty() {
        def input = [string: ['hello'], integer: ['10']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pojo = form as TestPOGOIgnoreProperty
        assert !pojo.string
        assert pojo.integer == 10
    }

}