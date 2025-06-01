package com.org.server_service.controller;

import com.org.server_service.model.EncryptedMessageDTO;
import com.org.server_service.service.MessageProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerMessageController {
    private final MessageProcessingService service;

    @PostMapping("/message")
    public ResponseEntity<String> receiveEncrypted(@RequestBody EncryptedMessageDTO dto) {
        String plaintext = service.process(dto);
        return ResponseEntity.ok(plaintext);
    }
}
