package ua.yuriih.crypto.lab3;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class NHashTest {
    private static final int TESTS_NUM = 1000;
    private static final Random RNG = new Random();

    @Test
    void hash_collisionUnlikely() throws IOException {
        byte[][] knownHashes = new byte[TESTS_NUM][];
        for (int test = 0; test < TESTS_NUM; test++) {
            int size = (50 + RNG.nextInt(50)) * 16;
            byte[] data = new byte[size];
            RNG.nextBytes(data);
//            System.err.println("----");
//            System.err.println(Arrays.toString(data));

            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            knownHashes[test] = NHash.hash(inputStream);
//            System.err.println(Arrays.toString(knownHashes[test]));

            for (int pastTest = 0; pastTest < test; pastTest++)
                assertFalse(Arrays.equals(knownHashes[pastTest], knownHashes[test]));
        }
    }

    @Test
    void hash_sameOutputForSameInput() throws IOException {
        for (int test = 0; test < TESTS_NUM; test++) {
            int size = RNG.nextInt(50) * 16;
            byte[] data = new byte[size];
            RNG.nextBytes(data);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            byte[] hash1 = NHash.hash(inputStream);
            inputStream = new ByteArrayInputStream(data);
            byte[] hash2 = NHash.hash(inputStream);

            assertArrayEquals(hash1, hash2);
        }
    }
}