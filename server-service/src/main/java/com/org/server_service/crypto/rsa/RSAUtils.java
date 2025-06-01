package com.org.server_service.crypto.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Для расшифровки закрытого ключа
 */
public class RSAUtils {
    private static final SecureRandom rnd = new SecureRandom();
    private static final BigInteger PUBLIC_EXPONENT = BigInteger.valueOf(65537);

    public static class RSAKeyPair {
        public final BigInteger e;
        public final BigInteger d;
        public final BigInteger n;

        public RSAKeyPair(BigInteger e, BigInteger d, BigInteger n) {
            this.e = e;
            this.d = d;
            this.n = n;
        }
    }

    /**
     * Генерирует пару RSA-ключей.
     *
     * @param bitLength длина в битах для каждого из простых p и q.
     *                  Итоговый модуль n будет иметь примерно 2*bitLength бит.
     *                  Рекомендуется значение >= 64, чтобы n > 2^64 (требуется для 8-байтового DES-ключа).
     * @return RSAKeyPair, содержащий публичный ключ (e, n) и приватный ключ (d, n).
     */
    public static RSAKeyPair generateRSAKeyPair(int bitLength) {
        // 1) Генерируем два больших простых p и q длины bitLength бит.
        BigInteger p = BigInteger.probablePrime(bitLength, rnd);
        BigInteger q;
        do {
            q = BigInteger.probablePrime(bitLength, rnd);
        } while (q.equals(p));

        // 2) Вычисляем модуль n = p * q
        BigInteger n = p.multiply(q);

        // 3) Вычисляем phi(n) = (p - 1) * (q - 1)
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // 4) Выбираем публичную экспоненту e = 65537 (если gcd(e, phi) != 1, подбираем другую)
        BigInteger e = PUBLIC_EXPONENT;
        if (!e.gcd(phi).equals(BigInteger.ONE)) {
            // Если 65537 не взаимно прост с phi, ищем следующее нечётное
            e = BigInteger.valueOf(3);
            while (!e.gcd(phi).equals(BigInteger.ONE)) {
                e = e.add(BigInteger.valueOf(2));
            }
        }

        // 5) Вычисляем приватную экспоненту d = e^(-1) mod phi
        BigInteger d = e.modInverse(phi);

        return new RSAKeyPair(e, d, n);
    }

    /**
     * Шифрование целого plaintext (BigInteger) по формуле ciphertext = plaintext^e mod n.
     *
     * @throws IllegalArgumentException если plaintext >= n.
     */
    public static BigInteger rsaEncrypt(BigInteger plaintext, BigInteger e, BigInteger n) {
        if (plaintext.compareTo(n) >= 0) {
            throw new IllegalArgumentException("Plaintext must be less than modulus n.");
        }
        return plaintext.modPow(e, n);
    }

    /**
     * Расшифровка ciphertext по формуле plaintext = ciphertext^d mod n.
     */
    public static BigInteger rsaDecrypt(BigInteger ciphertext, BigInteger d, BigInteger n) {
        return ciphertext.modPow(d, n);
    }

    public static void main(String[] args) {
        RSAKeyPair keys = generateRSAKeyPair(64);
        System.out.println("Public Key (e, n): " + keys.e + ", " + keys.n);
        System.out.println("Private Key (d, n): " + keys.d + ", " + keys.n);

        BigInteger message = BigInteger.valueOf(42);
        BigInteger encrypted = rsaEncrypt(message, keys.e, keys.n);
        System.out.println("Encrypted: " + encrypted);

        BigInteger decrypted = rsaDecrypt(encrypted, keys.d, keys.n);
        System.out.println("Decrypted: " + decrypted);
    }
}
