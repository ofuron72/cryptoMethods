package com.org.server_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EncryptedMessageDTO {
    private String encryptedMessage; //зашифрованное сообщение
    private String encryptedKey; //зашифрованный DES-ключ
    private String r; // r-координата подписи EGSA (десятичная строка)
    private String s; // s-координата подписи EGSA (десятичная строка)
}
