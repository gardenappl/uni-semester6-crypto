package ua.yuriih.crypto.lab1;

import java.math.BigInteger;
import java.util.Random;

public class MillerRabin {
    private static final Random RNG = new Random();
    private static final BigInteger THREE = BigInteger.valueOf(3);

    /**
     * For an odd integer n > 2, let's write n as 2^s * d + 1 where d is odd and s, d > 0.
     * Consider integer a, called a base, such that 0 < a < n. Then, n is said to be a
     * <strong>strong probable prime to base a</strong> if one of these congruence relations holds:
     * a^d == 1 (mod n)
     * a^(2^r * d) == -1 (mod n) for some 0 <= r < s.
     *
     * @param n value to test for primality
     * @param steps how many steps to run, each step increases the chances of filtering out composite numbers.
     * @return true if "probably prime", false if definitely composite
     */
    public static boolean isProbablePrime(BigInteger n, int steps) {
        if (n.compareTo(THREE) <= 0) {
            return n.compareTo(BigInteger.ONE) > 0;
        }

        BigInteger nMinusOne = n.subtract(BigInteger.ONE);

        BigInteger d = nMinusOne;
        int s = 0;
        while (true) {
            BigInteger[] quotientAndRemainder = d.divideAndRemainder(BigInteger.TWO);
            if (quotientAndRemainder[1].equals(BigInteger.ZERO)) {
                d = quotientAndRemainder[0];
                s++;
            } else {
                break;
            }
        }

        int step = 0;

        WitnessLoop:
        while (step < steps) {
            BigInteger a = new BigInteger(n.bitLength(), RNG);
            if (a.compareTo(BigInteger.ONE) <= 0 || a.compareTo(nMinusOne) >= 0)
                continue;

            step++;

            BigInteger x = a.modPow(d, n);
            if (x.equals(BigInteger.ONE) || x.equals(nMinusOne))
                continue;

            for (int i = 0; i < s - 1; i++) {
                x = x.modPow(BigInteger.TWO, n);
                if (x.equals(nMinusOne))
                    continue WitnessLoop;
            }
            return false;
        }
        return true;
    }
}
