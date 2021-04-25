package ua.yuriih.crypto.lab1;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class ProbablyPrimeTest {
    private static final int TEST_STEPS = 1000;

    @Test
    void isProbablePrime_edgeCases() {
        for (int i = -10; i <= 1; i++) {
            assertFalse(Fermat.isProbablePrime(BigInteger.valueOf(i), 0));
            assertFalse(MillerRabin.isProbablePrime(BigInteger.valueOf(i), 0));
        }
        for (int i = 2; i <= 3; i++) {
            assertTrue(Fermat.isProbablePrime(BigInteger.valueOf(i), 0));
            assertTrue(MillerRabin.isProbablePrime(BigInteger.valueOf(i), 0));
        }
    }

    @Test
    void isProbablePrime_checkPrimes() {
        final long[] TEST_PRIMES = { 11, 17, 101, Integer.MAX_VALUE, 2305843009213693951L };
        for (long testPrime : TEST_PRIMES) {
            assertTrue(Fermat.isProbablePrime(BigInteger.valueOf(testPrime), TEST_STEPS));
            assertTrue(MillerRabin.isProbablePrime(BigInteger.valueOf(testPrime), TEST_STEPS));
        }
    }

    @Test
    void isProbablePrime_checkComposites() {
        final long[] TEST_COMPOSITES = { 1869, 2021, 4, 8, 16 };
        for (long testComposite : TEST_COMPOSITES) {
            assertFalse(Fermat.isProbablePrime(BigInteger.valueOf(testComposite), TEST_STEPS));
            assertFalse(MillerRabin.isProbablePrime(BigInteger.valueOf(testComposite), TEST_STEPS));
        }
    }
}