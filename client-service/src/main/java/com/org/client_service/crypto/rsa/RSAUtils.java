package com.org.client_service.crypto.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Для расшифровки закрытого ключа
 */
public class RSAUtils {
    private static final SecureRandom rnd = new SecureRandom();
    private static final BigInteger PUBLIC_EXPONENT = BigInteger.valueOf(65537);

    /**
     * Класс для хранения ключевой пары RSA:
     * e — открытый экспонент,
     * d — приватный ключ (секретный экспонент),
     * n — модуль (общее основание для e и d).
     */
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
     * Генерация RSA-ключей заданной битовой длины.
     * @param bitLength — длина одного простого числа (итоговый модуль n будет примерно в 2 раза длиннее).
     * @return объект RSAKeyPair с открытым и закрытым ключами.
     */
    public static RSAKeyPair generateRSAKeyPair(int bitLength) {

        // Генерация первого простого числа p
        BigInteger p = BigInteger.probablePrime(bitLength, rnd);
        BigInteger q;
        // Генерация второго простого числа q, отличного от p
        do {
            q = BigInteger.probablePrime(bitLength, rnd);
        } while (q.equals(p));

        // Вычисляем модуль n = p * q
        BigInteger n = p.multiply(q);

        // Вычисляем функцию Эйлера φ(n) = (p - 1)(q - 1)
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // Используем стандартный показатель e, если он взаимно прост с φ(n)
        BigInteger e = PUBLIC_EXPONENT;
        if (!e.gcd(phi).equals(BigInteger.ONE)) {
            e = BigInteger.valueOf(3);
            while (!e.gcd(phi).equals(BigInteger.ONE)) {
                e = e.add(BigInteger.valueOf(2));
            }
        }

        // Вычисляем приватную экспоненту d как обратную к e по модулю φ(n)
        BigInteger d = e.modInverse(phi);

        return new RSAKeyPair(e, d, n);
    }

    /**
     * Шифрование сообщения с использованием открытого ключа (e, n).
     * @param plaintext — исходное сообщение в виде BigInteger
     * @param e — открытый показатель
     * @param n — модуль
     * @return зашифрованное сообщение
     */
    public static BigInteger rsaEncrypt(BigInteger plaintext, BigInteger e, BigInteger n) {
        if (plaintext.compareTo(n) >= 0) {
            throw new IllegalArgumentException("Plaintext must be less than modulus n.");
        }
        return plaintext.modPow(e, n);
    }

    /**
     * Расшифровка зашифрованного сообщения с использованием приватного ключа (d, n).
     * @param ciphertext — зашифрованный текст
     * @param d — приватный показатель
     * @param n — модуль
     * @return расшифрованное сообщение
     */
    public static BigInteger rsaDecrypt(BigInteger ciphertext, BigInteger d, BigInteger n) {
        return ciphertext.modPow(d, n);
    }
}
