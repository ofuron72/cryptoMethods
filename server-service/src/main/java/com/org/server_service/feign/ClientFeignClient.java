package com.org.server_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="server-service",url = "http://localhost:8080")
public interface ClientFeignClient {

    @PostMapping("/receive")
    String sendResponse(@RequestBody String responseMessage);
}

