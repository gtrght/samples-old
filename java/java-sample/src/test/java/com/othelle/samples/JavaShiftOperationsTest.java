package com.othelle.samples;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.toBinaryString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * author: Vasily Vlasov date: 12/2/13
 */
public class JavaShiftOperationsTest {

    public void testShift(int value, int argument, int expectedValue, ShiftTransform transform) {
        assertThat(Integer.MAX_VALUE << 1, Matchers.equalTo(-2));

        String format = String.format("%s %s %s = %s", toBinaryString(value), transform.getOperation(), argument,
                toBinaryString(transform.apply(value, argument)));

        System.out.println(format);
        assertThat(format, transform.apply(value, argument), Matchers.equalTo(expectedValue));
    }

    @Test
    public void testLeftShift() {
        ShiftTransform transform = new LeftShift();
        testShift(Integer.MAX_VALUE, 1, -2, transform);
        testShift(0, 1, 0, transform);
        testShift(Integer.MAX_VALUE, 31, Integer.MIN_VALUE, transform);
        testShift(Integer.MAX_VALUE, 32, Integer.MAX_VALUE, transform);
        testShift(Integer.MAX_VALUE, 33, Integer.MAX_VALUE << 1, transform);
        testShift(Integer.MIN_VALUE, 1, 0, transform);
    }

    @Test
    public void testRightShift() {
        ShiftTransform transform = new RightShift();
        testShift(0, 1, 0, transform); // 0 >> n = 0
        testShift(-1, 1, -1, transform); //11111111111111111111111111111111 >> 1 = 11111111111111111111111111111111
        testShift(Integer.MIN_VALUE, 1, -1073741824, transform); //10000000000000000000000000000000 >> 1 = 11000000000000000000000000000000
        testShift(Integer.MAX_VALUE, 1, MAX_VALUE / 2, transform);
        testShift(Integer.MAX_VALUE, 31, 0, transform);
        testShift(Integer.MAX_VALUE, 32, MAX_VALUE, transform);
        testShift(Integer.MAX_VALUE, 33, Integer.MAX_VALUE >> 1, transform);
    }

    @Test
    public void testRightShiftUnsigned() {
        ShiftTransform transform = new RightUnsignedShift();
        testShift(0, 1, 0, transform); // 0 >> n = 0
        testShift(-1, 1, MAX_VALUE, transform);
        testShift(Integer.MIN_VALUE, 1, Math.abs(Integer.MIN_VALUE / 2), transform); //10000000000000000000000000000000 >> 1 = 01000000000000000000000000000000
        testShift(Integer.MAX_VALUE, 1, MAX_VALUE / 2, transform);
        testShift(Integer.MAX_VALUE, 31, 0, transform);
        testShift(Integer.MAX_VALUE, 32, MAX_VALUE, transform);
        testShift(Integer.MAX_VALUE, 33, Integer.MAX_VALUE >> 1, transform);
    }

    interface ShiftTransform {
        String getOperation();
        int apply(int value, int argument);
    }

    static class LeftShift implements ShiftTransform {

        @Override
        public String getOperation() {
            return "<<";
        }

        @Override
        public int apply(int value, int argument) {
            return value << argument;
        }
    }

    static class RightShift implements ShiftTransform {
        @Override
        public String getOperation() {
            return ">>";
        }

        @Override
        public int apply(int value, int argument) {
            return value >> argument;
        }
    }

    static class RightUnsignedShift implements ShiftTransform {
        @Override
        public String getOperation() {
            return ">>>";
        }

        @Override
        public int apply(int value, int argument) {
            return value >>> argument;
        }
    }
}
