package com.org.client_service.feign;

import com.org.client_service.model.EncryptedMessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "server-service", url = "http://localhost:8081")

public interface ClientFeignSender {
    @PostMapping("/server/message")
    String sendToServer(EncryptedMessageDTO dto);
}
