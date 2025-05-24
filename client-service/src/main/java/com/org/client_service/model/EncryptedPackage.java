package com.org.client_service.model;

import lombok.Data;

@Data
public class EncryptedPackage {
    private byte[] encryptedMessage;
    private byte[] encryptedDesKey;
    private byte[] signature;
}
