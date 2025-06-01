package com.org.client_service.controller;

import com.org.client_service.feign.ClientFeignSender;
import com.org.client_service.model.EncryptedMessageDTO;
import com.org.client_service.model.PlainTextDTO;
import com.org.client_service.service.MessageBuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientMessageController {
    private final MessageBuildService buildService;
    private final ClientFeignSender feignSender;



    @PostMapping("/send")
    public ResponseEntity<String> sendPlainText(@RequestBody PlainTextDTO plainText) {
        try {
            EncryptedMessageDTO dto = buildService.build(plainText);
            // Отправляем DTO на /server/message
            String response = feignSender.sendToServer(dto);
            System.out.println(dto);
            return ResponseEntity.ok("Ответ от сервера: " + response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }
}
