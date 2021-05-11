package ua.yuriih.crypto.lab2;

public final class Cobra24_128 {

    private static final int BLOCK_SIZE = 4;
    private static final int ROUNDS = 24;

    public static final int MAX_KEY_SIZE = ROUNDS / 2 * (BLOCK_SIZE - 1);

    private final int[][] P; //24 x 3 permutation boxes
    private final int[][] S; //4 x 256 substitution boxes
    private final int[][] W; //2 x 4 whitening, 1 for input and 1 for output

    public Cobra24_128(int[] privateKey) {
        if (privateKey.length == 0 || privateKey.length > MAX_KEY_SIZE)
            throw new IllegalArgumentException("Bad key size, should be between 1 and " + MAX_KEY_SIZE);

        int[] key = new int[privateKey.length];
        System.arraycopy(privateKey, 0, key, 0, privateKey.length);

        P = new int[ROUNDS][BLOCK_SIZE - 1];
        S = new int[BLOCK_SIZE][256];
        W = new int[2][BLOCK_SIZE];

        //i = index in PI
        int i = 0;
        for (int p = 0; p < P.length; p++) {
            System.arraycopy(MathUtils.PI, i, P[p], 0, BLOCK_SIZE - 1);
            i += BLOCK_SIZE - 1;
        }
        for (int s = 0; s < S.length; s++) {
            System.arraycopy(MathUtils.PI, i, S[s], 0, 256);
            i += 256;
        }
        for (int w = 0; w < W.length; w++) {
            System.arraycopy(MathUtils.PI, i, W[w], 0, BLOCK_SIZE);
            i += BLOCK_SIZE;
        }

        for (int repeat = 0; repeat < 2; repeat++) {
            //step 2
            //i = index in key
            i = 0;
            for (int p = 0; p < P.length; p++) {
                for (int p2 = 0; p2 < P[p].length; p2++) {
                    P[p][p2] ^= key[i];
                    i = (i + 1) % key.length;
                }
            }

            //steps 3..6
            changeBoxes(P, key);

            //step 7: circular right shift integers in key, goto step 2.
            if (repeat == 0) {
                for (i = 0; i < key.length; i++)
                    key[i] = MathUtils.circularRightShift1(key[i]);
            }
        }
        //Step 8 (repeat steps 3..6 for P, S, W)
        changeBoxes(P, key);
        changeBoxes(S, key);
        changeBoxes(W, key);
    }

    private void changeBoxes(int[][] boxes, int[] key) {
        int[] buffer = new int[BLOCK_SIZE]; //zeros
        //i = index in buffer array
        int i = 0;
        for (int j = 0; j < boxes.length; j++) {
            for (int k = 0; k < boxes[j].length; k++) {
                if (i == 0)
                    buffer = encryptBlock(buffer);
                boxes[j][k] = buffer[i];
                i = (i + 1) % BLOCK_SIZE;
            }
        }
    }

    public int[] encryptBlock(int[] data) {
        int[] result = new int[BLOCK_SIZE];

        result[0] = data[0] ^ W[0][0];
        result[1] = data[1] ^ W[0][1];
        result[2] = data[2] ^ W[0][2];
        result[3] = data[3] ^ W[0][3];

        int[] nextResult = new int[BLOCK_SIZE];
        for (int i = 0; i < ROUNDS; i++) {
            nextResult[0] = result[3];
            nextResult[1] = MathUtils.circularRightShift1(result[0] ^ f(result[1], P[i][0]));
            nextResult[2] = MathUtils.circularRightShift1(result[1] ^ f(result[2], P[i][1]));
            nextResult[3] = MathUtils.circularRightShift1(result[2] ^ f(result[3], P[i][2]));

            System.arraycopy(nextResult, 0, result, 0, BLOCK_SIZE);
        }

        result[0] ^= W[1][0];
        result[1] ^= W[1][1];
        result[2] ^= W[1][2];
        result[3] ^= W[1][3];
        return result;
    }

    public int[] decryptBlock(int[] data) {
        int[] result = new int[BLOCK_SIZE];

        result[0] = data[0] ^ W[1][0];
        result[1] = data[1] ^ W[1][1];
        result[2] = data[2] ^ W[1][2];
        result[3] = data[3] ^ W[1][3];

        int[] nextResult = new int[BLOCK_SIZE];
        for (int i = ROUNDS - 1; i >= 0; i--) {
            nextResult[3] = result[0];
            nextResult[2] = MathUtils.circularLeftShift1(result[3]) ^ f(nextResult[3], P[i][2]);
            nextResult[1] = MathUtils.circularLeftShift1(result[2]) ^ f(nextResult[2], P[i][1]);
            nextResult[0] = MathUtils.circularLeftShift1(result[1]) ^ f(nextResult[1], P[i][0]);

            System.arraycopy(nextResult, 0, result, 0, BLOCK_SIZE);
        }

        result[0] ^= W[0][0];
        result[1] ^= W[0][1];
        result[2] ^= W[0][2];
        result[3] ^= W[0][3];
        return result;
    }

    private int f(int x, int p) {
        int z = x ^ p;
        return ((S[0][(z >> 24) & 0xff] +
                S[1][(z >> 16) & 0xff]) ^
                S[2][(z >> 8) & 0xff]) +
                S[3][z & 0xff];
    }
}
