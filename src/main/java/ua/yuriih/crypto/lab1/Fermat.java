package ua.yuriih.crypto.lab1;

import ua.yuriih.crypto.MathUtils;

import java.math.BigInteger;
import java.util.Random;

public class Fermat {
    private static final Random RNG = new Random();

    /**
     * Repeat k times:
     *   Pick randomly in the range [2, n-2]
     *   If a^(n-1) != 1 (mod n) then return "composite"
     * Otherwise it's "probably prime"
     *
     * @param n value to test for primality
     * @param steps how many steps to run, each step increases the chances of filtering out composite numbers.
     * @return true if "probably prime", false if definitely composite
     */
    public static boolean isProbablePrime(BigInteger n, int steps) {
        if (n.compareTo(MathUtils.THREE) <= 0) {
            return n.compareTo(BigInteger.TWO) >= 0;
        }

        BigInteger nMinusOne = n.subtract(BigInteger.ONE);

        int step = 0;
        while (step < steps) {
            BigInteger a = new BigInteger(n.bitLength(), RNG);
            if (a.compareTo(BigInteger.ONE) <= 0 || a.compareTo(nMinusOne) >= 0)
                continue;

            if (!a.modPow(nMinusOne, n).equals(BigInteger.ONE))
                return false;
            step++;
        }
        return true;
    }
}
