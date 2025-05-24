package com.org.server_service.model;

import lombok.Data;

@Data
public class EncryptedPackage {
    private byte[] encryptedMessage;
    private byte[] encryptedDesKey;
    private byte[] signature;
}
