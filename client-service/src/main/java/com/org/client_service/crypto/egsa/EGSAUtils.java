package com.org.client_service.crypto.egsa;

import java.math.BigInteger;
import java.util.Random;

/**
 * Проверка подписи
 */
public class EGSAUtils {
    static final Random rnd = new Random();

    /**
     * Класс для хранения пары ключей:
     * p — большое простое число (модуль),
     * g — примитивный корень по модулю p (база),
     * x — секретный ключ (приватный),
     * y — открытый ключ (публичный).
     */
    public static class EGSAKeyPair {
        public final BigInteger p, g, x, y;
        public EGSAKeyPair(BigInteger p, BigInteger g, BigInteger x, BigInteger y) {
            this.p = p;
            this.g = g;
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Класс для хранения подписи:
     * r и s — компоненты цифровой подписи.
     */
    public static class EGSignature {
        public final BigInteger r, s;
        public EGSignature(BigInteger r, BigInteger s) {
            this.r = r;
            this.s = s;
        }
    }

    /**
     * Генерация большого вероятно простого числа заданной битовой длины.
     * Метод использует вероятностный тест на простоту.
     */
    public static BigInteger generatePrime(int bitLength) {
        while (true) {
            BigInteger p = new BigInteger(bitLength, rnd);
            if (p.isProbablePrime(20)) return p;
        }
    }

    /**
     * Генерация ключевой пары для алгоритма подписи Эль-Гамаля.
     * @param bitLength длина простого числа p в битах.
     * @return объект ключевой пары EGSAKeyPair
     */
    public static EGSAKeyPair generateKeys(int bitLength) {
        BigInteger p = generatePrime(bitLength);  // Простое число p
        BigInteger g = BigInteger.valueOf(2); // можно выбрать более надёжный g
        BigInteger x = new BigInteger(bitLength - 2, rnd); // Приватный ключ x
        BigInteger y = g.modPow(x, p);  // Публичный ключ y = g^x mod p
        return new EGSAKeyPair(p, g, x, y);
    }

    public static EGSignature sign(BigInteger m, EGSAKeyPair keyPair) {
        BigInteger p = keyPair.p;
        BigInteger g = keyPair.g;
        BigInteger x = keyPair.x;
        BigInteger p1 = p.subtract(BigInteger.ONE); // φ(p) = p - 1 для простого p

        BigInteger k;
        // Генерация случайного k, взаимно простого с (p - 1)
        do {
            k = new BigInteger(p.bitLength() - 1, rnd);
        } while (!k.gcd(p1).equals(BigInteger.ONE));

        BigInteger r = g.modPow(k, p);  // r = g^k mod p
        BigInteger kInv = k.modInverse(p1); // k^(-1) mod (p - 1)
        BigInteger s = (m.subtract(x.multiply(r))).multiply(kInv).mod(p1);  // s = (m - xr) * k^(-1) mod (p - 1)

        return new EGSignature(r, s);
    }

    /**
     * Проверка подписи (r, s) на сообщение m.
     * @param m — сообщение
     * @param sig — подпись (r, s)
     * @param keyPair — публичные параметры p, g, y
     * @return true, если подпись верна; иначе — false
     */
    public static boolean verify(BigInteger m, EGSignature sig, EGSAKeyPair keyPair) {
        BigInteger p = keyPair.p;
        BigInteger g = keyPair.g;
        BigInteger y = keyPair.y;

        // Вычисляем левую и правую части уравнения проверки
        BigInteger left = g.modPow(m, p);
        BigInteger right = y.modPow(sig.r, p).multiply(sig.r.modPow(sig.s, p)).mod(p);

        // Сравниваем: если равны — подпись верна
        return left.equals(right);
    }
}
