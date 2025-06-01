package com.org.client_service.crypto.egsa;

import java.math.BigInteger;
import java.util.Random;

/**
 * Проверка подписи
 */
public class EGSAUtils {
    static final Random rnd = new Random();

    public static class EGSAKeyPair {
        public final BigInteger p, g, x, y;
        public EGSAKeyPair(BigInteger p, BigInteger g, BigInteger x, BigInteger y) {
            this.p = p;
            this.g = g;
            this.x = x;
            this.y = y;
        }
    }

    public static class EGSignature {
        public final BigInteger r, s;
        public EGSignature(BigInteger r, BigInteger s) {
            this.r = r;
            this.s = s;
        }
    }

    public static BigInteger generatePrime(int bitLength) {
        while (true) {
            BigInteger p = new BigInteger(bitLength, rnd);
            if (p.isProbablePrime(20)) return p;
        }
    }

    public static EGSAKeyPair generateKeys(int bitLength) {
        BigInteger p = generatePrime(bitLength);
        BigInteger g = BigInteger.valueOf(2); // можно выбрать более надёжный g
        BigInteger x = new BigInteger(bitLength - 2, rnd);
        BigInteger y = g.modPow(x, p);
        return new EGSAKeyPair(p, g, x, y);
    }

    public static EGSignature sign(BigInteger m, EGSAKeyPair keyPair) {
        BigInteger p = keyPair.p;
        BigInteger g = keyPair.g;
        BigInteger x = keyPair.x;
        BigInteger p1 = p.subtract(BigInteger.ONE);

        BigInteger k;
        do {
            k = new BigInteger(p.bitLength() - 1, rnd);
        } while (!k.gcd(p1).equals(BigInteger.ONE));

        BigInteger r = g.modPow(k, p);
        BigInteger kInv = k.modInverse(p1);
        BigInteger s = (m.subtract(x.multiply(r))).multiply(kInv).mod(p1);

        return new EGSignature(r, s);
    }

    // Проверка подписи
    public static boolean verify(BigInteger m, EGSignature sig, EGSAKeyPair keyPair) {
        BigInteger p = keyPair.p;
        BigInteger g = keyPair.g;
        BigInteger y = keyPair.y;

        BigInteger left = g.modPow(m, p);
        BigInteger right = y.modPow(sig.r, p).multiply(sig.r.modPow(sig.s, p)).mod(p);
        return left.equals(right);
    }
}
