package ua.yuriih.crypto;

import org.junit.jupiter.api.Test;
import ua.yuriih.crypto.MathUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MathUtilsTest {
    private static final Random RNG = new Random();

    @Test
    void toByteArray() {
        for (int size = 0; size < 20; size += 4) {
            byte[] bytes = new byte[size];
            int[] ints = new int[size / 4];
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

            for (int test = 0; test < 1000; test++) {
                RNG.nextBytes(byteBuffer.array());

                for (int i = 0; i < ints.length; i++)
                    ints[i] = byteBuffer.getInt();
                byteBuffer.rewind();

                byte[] resultBytes = MathUtils.toByteArray(ints);
                assertArrayEquals(bytes, resultBytes);
            }
        }
    }

    @Test
    void toIntArray_throwsOnWrongSize() {
        for (int i = 0; i < 10; i++) {
            for (int j = 1; j < 4; j++) {
                byte[] bytes = new byte[i * 4 + j];
                assertThrows(IllegalArgumentException.class, () -> {
                    MathUtils.toIntArray(bytes);
                });
            }
        }
    }

    @Test
    void toIntArray_toByteArray() {
        for (int size = 1; size < 10; size++) {
            byte[] bytes = new byte[size * 4];

            for (int test = 0; test < 1000; test++) {
                RNG.nextBytes(bytes);

                int[] resultInts = MathUtils.toIntArray(bytes);
                byte[] resultBytes = MathUtils.toByteArray(resultInts);
                assertArrayEquals(bytes, resultBytes);
            }
        }
    }
}
