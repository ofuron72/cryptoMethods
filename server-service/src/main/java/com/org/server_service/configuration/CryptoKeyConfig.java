package com.org.server_service.configuration;

import com.org.server_service.crypto.egsa.EGSAUtils;
import com.org.server_service.crypto.rsa.RSAUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptoKeyConfig {
    @Bean("serverRsaKeyPair")
    public RSAUtils.RSAKeyPair serverRsaKeyPair() {
        // rsa ключи
        return RSAUtils.generateRSAKeyPair(64);
    }

    @Bean("serverEgsaKeyPair")
    public EGSAUtils.EGSAKeyPair serverEgsaKeyPair() {
        return EGSAUtils.generateKeys(256);
    }
}
