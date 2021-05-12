package ua.yuriih.crypto.lab1;

import java.math.BigInteger;

public class Montgomery {
    /**
     * Wrapper for BigInteger in Montgomery form (aR mod N)
     */
    public class Integer {
        private final BigInteger t;

        /**
         * @param t in the range [0; N)
         */
        private Integer(BigInteger t) {
            this.t = t;
        }

        @Override
        public String toString() {
            return t.toString();
        }

        public Integer add(Integer num2) {
            BigInteger sum = t.add(num2.t);
            if (sum.compareTo(n) >= 0)
                sum = sum.subtract(n);
            return new Integer(sum);
        }

        public Integer subtract(Integer num2) {
            BigInteger diff = t.subtract(num2.t);
            if (diff.compareTo(BigInteger.ZERO) < 0)
                diff = n.add(diff);
            return new Integer(diff);
        }

        public Integer multiply(Integer num2) {
            return new Integer(redc(t.multiply(num2.t)));
        }

        /**
         * Binary modular exponentiation
         */
        public Integer pow(BigInteger exponent) {
            if (exponent.compareTo(BigInteger.ZERO) < 0)
                throw new UnsupportedOperationException("Exponent can't be negative");

            Integer product = one;
            Integer base = this;
            for (int i = 0; i < exponent.bitLength(); i++) {
                if (exponent.testBit(i))
                    product = product.multiply(base);
                base = base.multiply(base);
            }
            return product;
        }

        public BigInteger fromMontgomery() {
            return redc(t);
        }
    }

    /*
     * Montgomery reduction
     * Relies on integers R and N with gcd(R, N) = 1,
     * so that R is larger than N and is easy to divide with.
     */

    /**
     * A power of 2, greater than N.
     */
    private final BigInteger r;
    /**
     * R - 1, used as a bitmask
     */
    private final BigInteger rBitmask;
    /**
     * Modulus N
     */
    private final BigInteger n;
    /**
     * Integer in [0, R - 1] such that NN' = -1
     */
    private final BigInteger nPrime;

    /**
     * 1 in Montgomery form
     */
    private final Integer one;

    public Montgomery(BigInteger modulus) {
        if (!modulus.testBit(0) || modulus.compareTo(BigInteger.ONE) <= 0)
            throw new IllegalArgumentException("Modulus must be an odd integer >= 3");

        r = BigInteger.ONE.shiftLeft(modulus.bitLength());
        rBitmask = r.subtract(BigInteger.ONE);
        n = modulus;
        nPrime = n.modInverse(r).negate().mod(r);
        one = new Integer(r.mod(n));
    }

    public BigInteger getMod() {
        return n;
    }

    /**
     * Montgomery reduction
     * @param t T, integer in the range [0, RN - 1]
     * @return T * R^-1 mod N
     */
    private BigInteger redc(BigInteger t) {
        //m = ((T mod R) * N') mod R
        BigInteger m = t.and(rBitmask).multiply(nPrime).and(rBitmask);
        //result = (T + mN) / R
        BigInteger result = t.add(m.multiply(n)).shiftRight(rBitmask.bitLength());

        if (result.compareTo(n) >= 0)
            return result.subtract(n);
        else
            return result;
    }

    public Integer toMontgomery(BigInteger t) {
        return new Integer(redc(t.mod(n).multiply(r.modPow(BigInteger.TWO, n))));
    }
}
