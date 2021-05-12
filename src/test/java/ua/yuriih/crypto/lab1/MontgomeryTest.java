package ua.yuriih.crypto.lab1;

import org.junit.jupiter.api.Test;
import ua.yuriih.crypto.MathUtils;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MontgomeryTest {
    private void montgomery_testArithmetic(BigInteger a, BigInteger b, Montgomery mont) {
        BigInteger mod = mont.getMod();

        Montgomery.Integer aMont = mont.toMontgomery(a);
        Montgomery.Integer bMont = mont.toMontgomery(b);

        assertEquals(a.add(b).mod(mod), aMont.add(bMont).fromMontgomery(),
                a + " + " + b + " mod " + mod);
        assertEquals(a.subtract(b).mod(mod), aMont.subtract(bMont).fromMontgomery(),
                a + " - " + b + " mod " + mod);
        assertEquals(a.multiply(b).mod(mod), aMont.multiply(bMont).fromMontgomery(),
                a + " * " + b + " mod " + mod);
        if (b.signum() >= 0) {
            assertEquals(a.modPow(b, mod), aMont.pow(b).fromMontgomery(),
                    a + " ^ " + b + " mod " + mod);
        }
    }

    @Test
    public void montgomery_small() {
        Montgomery mont7 = new Montgomery(BigInteger.valueOf(7));
        Montgomery mont9 = new Montgomery(BigInteger.valueOf(9));
        for (BigInteger a = BigInteger.valueOf(-10); a.intValue() < 10; a = a.add(BigInteger.ONE)) {
            for (BigInteger b = BigInteger.valueOf(-10); b.intValue() < 10; b = b.add(BigInteger.ONE)) {
                montgomery_testArithmetic(a, b, mont7);
                montgomery_testArithmetic(a, b, mont9);
            }
        }
    }

    @Test
    public void montgomery_modulusRestrictions() {
        for (int i = -10; i < 3; i++) {
            BigInteger n = BigInteger.valueOf(i);
            assertThrows(IllegalArgumentException.class, () -> {
                new Montgomery(n);
            });
        }
        for (int i = 4; i < 20; i += 2) {
            BigInteger n = BigInteger.valueOf(i);
            assertThrows(IllegalArgumentException.class, () -> {
                new Montgomery(n);
            });
        }
    }

    @Test
    public void montgomery_large() {
        final Random rng = new Random();

        for (int i = 0; i < 100; i++) {
            BigInteger mod = new BigInteger(1024, rng);
            if (!mod.testBit(0) || mod.compareTo(MathUtils.THREE) < 0) {
                i--;
                continue;
            }
            BigInteger a = new BigInteger(1024, rng);
            BigInteger b = new BigInteger(1024, rng);
            Montgomery mont = new Montgomery(mod);

            montgomery_testArithmetic(a, b, mont);
        }
    }
}
