package com.org.server_service.controller;

import com.org.server_service.model.EncryptedPackage;
import com.org.server_service.util.DESUtil;
import com.org.server_service.util.EGSAUtil;
import com.org.server_service.util.RSAUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

@RestController
public class ReceiveController {
    private final KeyPair rsaKeyPair = RSAUtil.generateKeyPair();
    private final KeyPair egsaKeyPair = EGSAUtil.generateEGSAKeys();

    public ReceiveController() throws Exception {
    }

    @PostMapping("/receive")
    public String receive(@RequestBody EncryptedPackage pkg) throws Exception {
        SecretKey desKey = RSAUtil.decryptKey(pkg.getEncryptedDesKey(), rsaKeyPair.getPrivate());
        boolean valid = EGSAUtil.verify(desKey.getEncoded(), pkg.getSignature(), egsaKeyPair.getPublic());

        if (!valid) {
            return "Подпись недействительна!";
        }

        byte[] decryptedMessage = DESUtil.decrypt(pkg.getEncryptedMessage(), desKey);
        return "Расшифрованное сообщение: " + new String(decryptedMessage, StandardCharsets.UTF_8);
    }
}
