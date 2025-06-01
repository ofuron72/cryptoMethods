package com.org.client_service.service;

import com.org.client_service.crypto.des.DESUtils;
import com.org.client_service.crypto.egsa.EGSAUtils;
import com.org.client_service.crypto.rsa.RSAUtils;
import com.org.client_service.feign.ServerKeyClient;
import com.org.client_service.model.EncryptedMessageDTO;
import com.org.client_service.model.PlainTextDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageBuildService {
    private final ServerKeyClient keyClient;



    public EncryptedMessageDTO build(PlainTextDTO plainText) throws Exception {
        Map<String, String> publicKeys = keyClient.getPublicKeys();
        BigInteger rsaE = new BigInteger(publicKeys.get("rsaE"));
        BigInteger rsaN = new BigInteger(publicKeys.get("rsaN"));

        BigInteger egsaP = new BigInteger(publicKeys.get("egsaP"));
        BigInteger egsaG = new BigInteger(publicKeys.get("egsaG"));
        BigInteger egsaY = new BigInteger(publicKeys.get("egsaY"));
        BigInteger egsaX = new BigInteger(publicKeys.get("egsaX")); // приватный x, полученный от сервера

        EGSAUtils.EGSAKeyPair clientEgsaPair = new EGSAUtils.EGSAKeyPair(egsaP, egsaG, egsaX, egsaY);

        byte[] textBytes = plainText.getMessage()
                .getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] desKeyBytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            desKeyBytes[i] = (i < textBytes.length ? textBytes[i] : 0);
        }

        byte[] cipherDataBytes = DESUtils.desEncrypt(desKeyBytes, textBytes);
        String cipherDataBase64 = java.util.Base64
                .getEncoder()
                .encodeToString(cipherDataBytes);

        BigInteger desKeyBI = new BigInteger(1, desKeyBytes);
        BigInteger encryptedKeyBI = RSAUtils.rsaEncrypt(desKeyBI, rsaE, rsaN);

        EGSAUtils.EGSignature signature = EGSAUtils.sign(encryptedKeyBI, clientEgsaPair);

        //  Заполняеься DTO
        EncryptedMessageDTO dto = new EncryptedMessageDTO();
        dto.setEncryptedMessage(cipherDataBase64);           // шифрованные данные (DES)
        dto.setEncryptedKey(encryptedKeyBI.toString());      // зашифрованный RSA-ключ
        dto.setR(signature.r.toString());                    // r подписи
        dto.setS(signature.s.toString());                    // s подписи

        return dto;
    }
}
