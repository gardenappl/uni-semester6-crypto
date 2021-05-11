package ua.yuriih.crypto.lab2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MathUtilsTest {
    @Test
    void testCircularShiftRight1() {
        int i = 0x00000001;
        assertEquals(0x80000000, MathUtils.circularRightShift1(i));
        i = 0x10000001;
        assertEquals(0x88000000, MathUtils.circularRightShift1(i));
        i = 0x10000000;
        assertEquals(0x08000000, MathUtils.circularRightShift1(i));
    }

    @Test
    void testCircularShiftLeft1() {
        int i = 0x00000001;
        assertEquals(0x00000002, MathUtils.circularLeftShift1(i));
        i = 0x10000001;
        assertEquals(0x20000002, MathUtils.circularLeftShift1(i));
        i = 0xf0000000;
        assertEquals(0xe0000001, MathUtils.circularLeftShift1(i));
    }
}
