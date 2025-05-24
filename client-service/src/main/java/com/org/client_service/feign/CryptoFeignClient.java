package com.org.client_service.feign;

import com.org.client_service.model.EncryptedPackage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="server",url = "http://localhost:8081")
public interface CryptoFeignClient {
    String sendEncryptedPackage(@RequestBody EncryptedPackage encryptedPackage);
}
