package ua.yuriih.crypto.lab1;

import java.math.BigInteger;

public class ModPowBinary {
    /**
     * Convert e to binary notation: e = sum(a_i * 2^i), where a_i is a bit which is 0 or 1
     * Then b^e = b^sum(a_i * 2^i) = product(b^(a_i * 2^i))
     * @param n base
     * @param exponent exponent
     * @param m modulus
     * @return n^e mod m
     */
    public static BigInteger modPow(BigInteger n, BigInteger exponent, BigInteger m) {
        int modCompareToOne = m.compareTo(BigInteger.ONE);
        if (modCompareToOne == 0) //mod == 1
            return BigInteger.ZERO;
        else if (modCompareToOne < 0) //mod <= 0
            throw new IllegalArgumentException("Modulus must be a positive integer.");

        if (exponent.compareTo(BigInteger.ZERO) < 0)
            return modPow(n.modInverse(m), exponent.negate(), m);

        BigInteger base = n.mod(m);
        BigInteger result = BigInteger.ONE;
        while (exponent.compareTo(BigInteger.ZERO) > 0) {
            if (exponent.testBit(0))
                result = result.multiply(base).mod(m);

            exponent = exponent.shiftRight(1);
            base = base.modPow(BigInteger.TWO, m);
        }
        return result;
    }
}
