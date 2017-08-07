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
import ratpack.form.internal.DefaultForm
import ratpack.util.internal.ImmutableDelegatingMultiValueMap

import static java.util.Collections.emptyMap

@CompileStatic
public class TypeAssignableTest {

    final ImmutableDelegatingMultiValueMap EMPTY_MAP = new ImmutableDelegatingMultiValueMap(emptyMap())


    private static class TestString {
        String formProperty
    }

    @Test
    public void testString() {
        def input = [formProperty: ['test']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestString
        assert pogo.formProperty == 'test'
    }


    private static class TestInteger {
        Integer formProperty
    }

    @Test
    public void testInteger() {
        def input = [formProperty: ['1']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestInteger
        assert pogo.formProperty == 1
    }


    private static class TestLong {
        Long formProperty
    }

    @Test
    public void testLong() {
        def input = [formProperty: ['1']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestLong
        assert pogo.formProperty == 1
    }


    private static class TestDouble {
        Double formProperty
    }

    @Test
    public void testDouble() {
        def input = [formProperty: ['1']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestDouble
        assert pogo.formProperty == 1
    }


    private static class TestBigDecimal {
        BigDecimal formProperty
    }

    @Test
    public void testBigDecimal() {
        def input = [formProperty: ['1.33333333333333333333333333333333333']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestBigDecimal
        assert pogo.formProperty == 1.33333333333333333333333333333333333
    }


    private static class TestPrimitiveByte {
        byte formProperty
    }

    @Test
    public void testPrimitiveByte() {
        def input = [formProperty: ['1']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestPrimitiveByte
        assert pogo.formProperty == 0b00000001 as byte
    }

}
