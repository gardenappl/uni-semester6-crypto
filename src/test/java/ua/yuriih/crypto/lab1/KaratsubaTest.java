package ua.yuriih.crypto.lab1;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class KaratsubaTest {
    private static final Random RNG = new Random();

    @Test
    void multiply() {
        for (int i = 0; i < 1000; i++) {
            BigInteger b1 = new BigInteger(2048, RNG);
            BigInteger b2 = new BigInteger(2048, RNG);
            assertEquals(b1.multiply(b2), Karatsuba.multiply(b1, b2));
        }
    }
}
