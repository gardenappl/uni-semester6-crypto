package ua.yuriih.crypto.lab2;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class Cobra24_128Test {
    private static final int TESTS_PER_KEY_SIZE = 100;

    @Test
    void encryptAndDecryptShouldRecoverData() {
        Random rng = new Random();
        for (int keySize = 1; keySize < Cobra24_128.MAX_KEY_SIZE; keySize++) {
            int[] key = new int[keySize];
            for (int i = 0; i < keySize; i++)
                key[i] = rng.nextInt();

            Cobra24_128 cobra = new Cobra24_128(key);

            for (int testNum = 0; testNum < TESTS_PER_KEY_SIZE; testNum++) {
                int[] data = new int[4];
                data[0] = rng.nextInt();
                data[1] = rng.nextInt();
                data[2] = rng.nextInt();
                data[3] = rng.nextInt();

                int[] encryptedData = cobra.encryptBlock(data);
                int[] decryptedData = cobra.decryptBlock(encryptedData);

                assertArrayEquals(data, decryptedData);
                for (int i = 0; i < data.length; i++)
                    assertNotEquals(data[i], encryptedData[i]);
            }
        }
    }
}