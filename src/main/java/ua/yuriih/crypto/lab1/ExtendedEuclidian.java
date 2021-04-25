package ua.yuriih.crypto.lab1;

import java.math.BigInteger;

public class ExtendedEuclidian {
    public static class Result {
        public final BigInteger gcd;
        // Bezout coefficients
        public final BigInteger coefficientX;
        public final BigInteger coefficientY;

        private Result(BigInteger coefficientX, BigInteger coefficientY, BigInteger gcd) {
            this.coefficientX = coefficientX;
            this.coefficientY = coefficientY;
            this.gcd = gcd;
        }
    }

    /**
     * Finds GCD (Greatest Common Divisor) of x and y, as well as Bezout coefficients a and b:
     * ax + by = gcd(x, y)
     * @param x first number
     * @param y second number
     * @return Bezout coefficients and GCD
     */
    public static Result extendedGcd(BigInteger x, BigInteger y) {
        boolean flippedInputs = false;
        if (x.compareTo(y) > 0) {
            flippedInputs = true;
            BigInteger temp = y;
            y = x;
            x = temp;
        }
        BigInteger s = BigInteger.ZERO;
        BigInteger sOld = BigInteger.ONE;
        BigInteger r = y;
        BigInteger rOld = x;

        while (!r.equals(BigInteger.ZERO)) {
            BigInteger quotient = rOld.divide(r);
            BigInteger rNext = rOld.subtract(quotient.multiply(r));
            rOld = r;
            r = rNext;
            BigInteger sNext = sOld.subtract(quotient.multiply(s));
            sOld = s;
            s = sNext;
        }
        // Find the other coefficient
        BigInteger coefficientY;
        if (!y.equals(BigInteger.ZERO))
            coefficientY = rOld.subtract(sOld.multiply(x)).divide(y);
        else
            coefficientY = BigInteger.ZERO;

        // Always return positive GCD just like BigInteger.gcd
        BigInteger coefficientX = sOld;
        BigInteger gcd = rOld;
        if (gcd.compareTo(BigInteger.ZERO) < 0) {
            coefficientX = coefficientX.negate();
            coefficientY = coefficientY.negate();
            gcd = gcd.negate();
        }

        if (!flippedInputs)
            return new Result(coefficientX, coefficientY, gcd);
        else
            return new Result(coefficientY, coefficientX, gcd);
    }
}
