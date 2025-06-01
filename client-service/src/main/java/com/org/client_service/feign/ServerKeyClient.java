package com.org.client_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "serverService", url = "http://localhost:8081")
public interface ServerKeyClient {
    @GetMapping("/public/keys")
    Map<String, String> getPublicKeys();
}