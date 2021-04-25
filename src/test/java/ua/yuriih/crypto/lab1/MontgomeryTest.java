package ua.yuriih.crypto.lab1;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

public class MontgomeryTest {
    private void montgomery(long modulus) {
        final BigInteger mod = BigInteger.valueOf(modulus);

        Montgomery mont = new Montgomery(mod);
        for (BigInteger a = BigInteger.valueOf(-10); a.intValue() < 10; a = a.add(BigInteger.ONE)) {
            for (BigInteger b = BigInteger.valueOf(-10); b.intValue() < 10; b = b.add(BigInteger.ONE)) {
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
        }
    }

    @Test
    public void montgomery() {
        montgomery(7);
        montgomery(9);
    }
}
