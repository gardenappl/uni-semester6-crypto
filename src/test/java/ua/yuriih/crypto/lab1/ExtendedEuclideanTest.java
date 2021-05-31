package ua.yuriih.crypto.lab1;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ExtendedEuclideanTest {

    @Test
    void extendedGcd_small() {
        for (BigInteger x = BigInteger.valueOf(-50); x.longValue() < 50; x = x.add(BigInteger.ONE)) {
            for (BigInteger y = BigInteger.valueOf(-50); y.longValue() < 50; y = y.add(BigInteger.ONE)) {
                ExtendedEuclidean.Result result = ExtendedEuclidean.extendedGcd(x, y);
                assertEquals(x.gcd(y), result.gcd);
                assertEquals(
                        result.gcd.abs(),
                        result.coefficientX.multiply(x).add(result.coefficientY.multiply(y))
                );
            }
        }
    }

    @Test
    void extendedGcd_large() {
        Random rng = new Random();
        for (int i = 0; i < 100; i++) {
            BigInteger x = new BigInteger(1024, rng);
            BigInteger y = new BigInteger(1024, rng);
            ExtendedEuclidean.Result result = ExtendedEuclidean.extendedGcd(x, y);
            assertEquals(x.gcd(y), result.gcd);
            assertEquals(
                    result.gcd.abs(),
                    result.coefficientX.multiply(x).add(result.coefficientY.multiply(y))
            );
        }
    }
}