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
public class TypeAssignableViaSetTest {

    final ImmutableDelegatingMultiValueMap EMPTY_MAP = new ImmutableDelegatingMultiValueMap(emptyMap())


    @CompileStatic
    private static class TestStringViaSetter {
        String formProperty

        void setFormProperty(String v) {
            this.formProperty = v.reverse()
        }
    }

    @Test
    public void testStringViaSetter() {
        def input = [formProperty: ['test']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestStringViaSetter
        assert pogo.formProperty == 'tset'
    }


    private static class TestIntegerViaSetter {
        Integer formProperty

        void setFormProperty(String v) {
            formProperty = Integer.parseInt(v) + 1
        }
    }

    @Test
    public void testIntegerViaSetter() {
        def input = [formProperty: ['1']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestIntegerViaSetter
        assert pogo.formProperty == 2
    }


    private static class TestLongViaSetter {
        Long formProperty

        void setFormProperty(String v) {
            this.formProperty = Long.valueOf(v) + Integer.MAX_VALUE
        }
    }

    @Test
    public void testLongViaSetter() {
        def input = [formProperty: ['1']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestLongViaSetter
        assert pogo.formProperty == Integer.MAX_VALUE + 1L
    }


    private static class TestDoubleViaSetter {
        Double formProperty

        void setFormProperty(String v) {
            this.formProperty = Double.valueOf(v) + 0.1D
        }
    }

    @Test
    public void testDoubleViaSetter() {
        def input = [formProperty: ['1']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestDoubleViaSetter
        assert pogo.formProperty == 1.1
    }


    private static class TestBigDecimalViaSetter {
        BigDecimal formProperty

        void setFormProperty(String v) {
            this.formProperty = BigDecimal.ONE.add(new BigDecimal(v))
        }
    }

    @Test
    public void testBigDecimalViaSetter() {
        def input = [formProperty: ['1.1']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestBigDecimalViaSetter
        assert pogo.formProperty == 2.1
    }


    private static class TestPrimitiveByteViaSetter {
        byte formProperty

        void setFormProperty(String v) {
            this.formProperty = Byte.MIN_VALUE - Byte.valueOf(v)
        }
    }

    @Test
    public void testPrimitiveByteViaSetter() {
        def input = [formProperty: ['1']] as ImmutableDelegatingMultiValueMap

        def form = new DefaultForm(input, EMPTY_MAP)

        def pogo = form as TestPrimitiveByteViaSetter
        assert pogo.formProperty == 0b01111111 as byte
    }

}
