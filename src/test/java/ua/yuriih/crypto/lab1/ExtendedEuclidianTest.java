package ua.yuriih.crypto.lab1;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class ExtendedEuclidianTest {

    @Test
    void extendedGcd() {
        for (BigInteger x = BigInteger.valueOf(-50); x.longValue() < 50; x = x.add(BigInteger.ONE)) {
            for (BigInteger y = BigInteger.valueOf(-50); y.longValue() < 50; y = y.add(BigInteger.ONE)) {
                ExtendedEuclidian.Result result = ExtendedEuclidian.extendedGcd(x, y);
                assertEquals(x.gcd(y), result.gcd);
                assertEquals(
                        result.gcd.abs(),
                        result.coefficientX.multiply(x).add(result.coefficientY.multiply(y))
                );
            }
        }
    }
}