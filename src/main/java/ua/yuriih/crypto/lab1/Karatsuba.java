package ua.yuriih.crypto.lab1;

import java.math.BigInteger;

public class Karatsuba {
    /**
     * If x and y are n-digit strings in base B then:
     * x = x1 * B^m + x0
     * y = y1 * B^m + y0
     * xy = (x1 * B^m + x0)(y1 * B^m + y0) = z2 * B^2m + z1 * B^m + z0
     * where z2 = x1 * y1.
     *       z1 = x1 * y0 + x0 * y1.
     *       z0 = x0 * y0
     * We can re-write z1 = (x1 + x0)(y1 + y0) - z2 - z0
     * which results in only 3 multiplications instead of 4.
     * @param x first factor
     * @param y second factor
     * @return multiplication result
     */
    public static BigInteger multiply(BigInteger x, BigInteger y) {
        int length = Math.min(x.bitLength(), y.bitLength());
        if (length < 32)
            return x.multiply(y);

        int mid = length / 2;

        BigInteger xHigh = x.shiftRight(mid);
        BigInteger xLow = x.subtract(xHigh.shiftLeft(mid));
        BigInteger yHigh = y.shiftRight(mid);
        BigInteger yLow = y.subtract(yHigh.shiftLeft(mid));

        BigInteger z0 = multiply(xLow, yLow);
        BigInteger z2 = multiply(xHigh, yHigh);
        BigInteger z1 = multiply(xHigh.add(xLow), yHigh.add(yLow)).subtract(z0).subtract(z2);

        return z2.shiftLeft(mid * 2).add(z1.shiftLeft(mid)).add(z0);
    }
}
