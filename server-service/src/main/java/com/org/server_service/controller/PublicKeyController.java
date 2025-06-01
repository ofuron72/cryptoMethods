package com.org.server_service.controller;

import com.org.server_service.crypto.egsa.EGSAUtils;
import com.org.server_service.crypto.rsa.RSAUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController

public class PublicKeyController {
    private final RSAUtils.RSAKeyPair rsaKeyPair;
    private final EGSAUtils.EGSAKeyPair egsaKeyPair;

    //dependency injection
    public PublicKeyController(
            @Qualifier("serverRsaKeyPair") RSAUtils.RSAKeyPair rsaKeyPair,
            @Qualifier("serverEgsaKeyPair") EGSAUtils.EGSAKeyPair egsaKeyPair
    ) {
        this.rsaKeyPair = rsaKeyPair;
        this.egsaKeyPair = egsaKeyPair;
    }

    @GetMapping("/public/keys")
    public Map<String, String> getPublicKeys() {
        Map<String, String> keys = new HashMap<>();

        keys.put("rsaE", rsaKeyPair.e.toString());
        keys.put("rsaN", rsaKeyPair.n.toString());
        keys.put("egsaP", egsaKeyPair.p.toString());
        keys.put("egsaG", egsaKeyPair.g.toString());
        keys.put("egsaY", egsaKeyPair.y.toString());
        keys.put("egsaX", egsaKeyPair.x.toString());
        return keys;

    }
}
