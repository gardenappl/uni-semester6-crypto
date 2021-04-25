package ua.yuriih.crypto.lab1;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ModPowBinaryTest {
    private static final BigInteger TEST_MOD = BigInteger.valueOf(7);

    @Test
    void modPow_negativePower() {
        for (int i = 1; i < TEST_MOD.intValue(); i++) {
            assertEquals(
                    BigInteger.valueOf(i).modInverse(TEST_MOD),
                    ModPowBinary.modPow(BigInteger.valueOf(i), BigInteger.valueOf(-1), TEST_MOD)
            );

            assertEquals(
                    BigInteger.valueOf(i).modInverse(TEST_MOD).modPow(BigInteger.TWO, TEST_MOD),
                    ModPowBinary.modPow(BigInteger.valueOf(i), BigInteger.valueOf(-2), TEST_MOD)
            );
        }
    }

    @Test
    void modPow_negativeModulus() {
        assertThrows(IllegalArgumentException.class, () -> {
            ModPowBinary.modPow(BigInteger.TEN, BigInteger.TEN, BigInteger.valueOf(-1));
        });
    }

    @Test
    void modPow() {
        for (int i = -10; i < 10; i++) {
            for (int p = 0; p < 10; p++) {
                assertEquals(
                        BigInteger.valueOf(i).modPow(BigInteger.valueOf(p), TEST_MOD),
                        ModPowBinary.modPow(BigInteger.valueOf(i), BigInteger.valueOf(p), TEST_MOD)
                );
            }
        }
    }
}
