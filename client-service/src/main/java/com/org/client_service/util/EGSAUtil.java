package com.org.client_service.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class EGSAUtil {
    public static KeyPair generateEGSAKeys() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "BC");
        keyGen.initialize(1024);
        return keyGen.generateKeyPair();
    }

    public static byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signer = Signature.getInstance("SHA1withDSA", "BC");
        signer.initSign(privateKey);
        signer.update(data);
        return signer.sign();
    }

    public static boolean verify(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
        Signature verifier = Signature.getInstance("SHA1withDSA", "BC");
        verifier.initVerify(publicKey);
        verifier.update(data);
        return verifier.verify(signature);
    }
}
