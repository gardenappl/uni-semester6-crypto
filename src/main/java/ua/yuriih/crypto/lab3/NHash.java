package ua.yuriih.crypto.lab3;

import ua.yuriih.crypto.MathUtils;

import java.io.IOException;
import java.io.InputStream;

public final class NHash {
    private static final int[] v = new int[] {
            0xAAAAAAAA, 0xAAAAAAAA, 0xAAAAAAAA, 0xAAAAAAAA
    };

    private static final int[][] V = new int[8][];

    static {
        for (int i = 0; i < V.length; i++) {
            V[i] = new int[]{
                    4 * i + 1, 4 * i + 2, 4 * i + 3, 4 * i + 4
            };
        }
    }

    public static byte[] hash(InputStream in) throws IOException {
        byte[] blockBytes = new byte[16];
        int[] hash = new int[4];
        int[] gBuffer = new int[4];
        while (true) {
            int read = in.read(blockBytes);
            if (read == -1)
                return MathUtils.toByteArray(hash);
            else if (read < 16)
                throw new IllegalStateException("Length of input message should be 16*n bytes");

            int[] block = MathUtils.toIntArray(blockBytes);

            g(block, hash, gBuffer);
            MathUtils.xor(hash, gBuffer, hash);
            MathUtils.xor(hash, block, hash);
        }
    }

    private static void g(int[] blockInts, int[] hashInts, int[] resultArray) {
        //exchange lower and higher bits
        resultArray[0] = hashInts[2];
        resultArray[1] = hashInts[3];
        resultArray[2] = hashInts[0];
        resultArray[3] = hashInts[1];

        MathUtils.xor(resultArray, v, resultArray);
        MathUtils.xor(resultArray, blockInts, resultArray);

        int[] arg2 = new int[4];
        for (int i = 0; i < 8; i++) {
            MathUtils.xor(hashInts, V[i], arg2);
            processingStage(resultArray, arg2, resultArray);
        }
    }

    private static void processingStage(int[] x, int[] p, int[] resultArray) {
        int x0 = x[0];
        int x1 = x[1];

        int f0 = f(x0, p[0]) ^ x1;
        int f1 = f(f0, p[1]) ^ x0;

        int x2 = x[2] ^ f1;
        int x3 = x[3] ^ f0;

        int f2 = f(x2, p[2]) ^ x3;
        int f3 = f(f2, p[3]) ^ x2;

        x0 ^= f3;
        x1 ^= f2;

        resultArray[0] = x0;
        resultArray[1] = x1;
        resultArray[2] = x2;
        resultArray[3] = x3;
    }

    private static int f(int x, int p) {
        x ^= p;
        byte x0 = (byte) (x >> 24);
        byte x1 = (byte) (x >> 16);
        byte x2 = (byte) (x >> 8);
        byte x3 = (byte) x;

        x1 ^= x0;

        x2 ^= x3;

        x1++;
        x1 = s(x1, x2);

        x2 = s(x2, x1);

        x0 = s(x0, x1);

        x3++;
        x3 = s(x3, x2);

        return (x0 << 24) | (x1 << 16) | (x2 << 8) | x3;
    }

    private static byte s(byte b1, byte b2) {
        b1 += b2;
        //circular shift left
        return (byte) (b1 << 2 | (b1 >> (Byte.SIZE - 2)));
    }
}
