package com.org.client_service.crypto.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;

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

    public static RSAKeyPair generateRSAKeyPair(int bitLength) {

        BigInteger p = BigInteger.probablePrime(bitLength, rnd);
        BigInteger q;
        do {
            q = BigInteger.probablePrime(bitLength, rnd);
        } while (q.equals(p));

        BigInteger n = p.multiply(q);

        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger e = PUBLIC_EXPONENT;
        if (!e.gcd(phi).equals(BigInteger.ONE)) {
            e = BigInteger.valueOf(3);
            while (!e.gcd(phi).equals(BigInteger.ONE)) {
                e = e.add(BigInteger.valueOf(2));
            }
        }

        BigInteger d = e.modInverse(phi);

        return new RSAKeyPair(e, d, n);
    }

    public static BigInteger rsaEncrypt(BigInteger plaintext, BigInteger e, BigInteger n) {
        if (plaintext.compareTo(n) >= 0) {
            throw new IllegalArgumentException("Plaintext must be less than modulus n.");
        }
        return plaintext.modPow(e, n);
    }

    public static BigInteger rsaDecrypt(BigInteger ciphertext, BigInteger d, BigInteger n) {
        return ciphertext.modPow(d, n);
    }
}
