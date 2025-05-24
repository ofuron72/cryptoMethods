package com.org.client_service.controller;

import com.org.client_service.feign.CryptoFeignClient;
import com.org.client_service.model.EncryptedPackage;
import com.org.client_service.util.DESUtil;
import com.org.client_service.util.EGSAUtil;
import com.org.client_service.util.RSAUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

@RestController
@RequiredArgsConstructor
public class SendController {
    private final CryptoFeignClient cryptoFeignClient;

    public String sendMessage() throws Exception {
        String message = "Секретное сообщение";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

        SecretKey desKey = DESUtil.generateKey();
        byte[] encryptedMsg = DESUtil.encrypt(messageBytes, desKey);

        KeyPair rsaKeyPair = RSAUtil.generateKeyPair();
        byte[] encryptedKey = RSAUtil.encryptKey(desKey, rsaKeyPair.getPublic());

        KeyPair egsaKeyPair = EGSAUtil.generateEGSAKeys();
        byte[] signature = EGSAUtil.sign(desKey.getEncoded(), egsaKeyPair.getPrivate());

        EncryptedPackage pkg = new EncryptedPackage();
        pkg.setEncryptedMessage(encryptedMsg);
        pkg.setEncryptedDesKey(encryptedKey);
        pkg.setSignature(signature);

        return cryptoFeignClient.sendEncryptedPackage(pkg);
    }
}
