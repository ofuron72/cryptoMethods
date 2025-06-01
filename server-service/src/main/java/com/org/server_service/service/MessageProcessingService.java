package com.org.server_service.service;

import com.org.server_service.crypto.des.DESUtils;
import com.org.server_service.crypto.egsa.EGSAUtils;
import com.org.server_service.crypto.rsa.RSAUtils;
import com.org.server_service.model.EncryptedMessageDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class MessageProcessingService {
    private final RSAUtils.RSAKeyPair rsaKeyPair; //приватный и публичный RSA у сервера
    private final EGSAUtils.EGSAKeyPair egsaKeyPair;//приватный и публичный EGSA у сервера


    public MessageProcessingService(
            @Qualifier("serverRsaKeyPair") RSAUtils.RSAKeyPair rsaKeyPair,
            @Qualifier("serverEgsaKeyPair") EGSAUtils.EGSAKeyPair egsaKeyPair
    ) {
        this.rsaKeyPair = rsaKeyPair;
        this.egsaKeyPair = egsaKeyPair;
    }

    public String process(EncryptedMessageDTO dto) {
        try {
            BigInteger encryptedKeyBI = new BigInteger(dto.getEncryptedKey());

            EGSAUtils.EGSignature signature = new EGSAUtils.EGSignature(
                    new BigInteger(dto.getR()),
                    new BigInteger(dto.getS())
            );
            boolean valid = EGSAUtils.verify(encryptedKeyBI, signature, egsaKeyPair);
            if (!valid) {
                return "Invalid EGSA signature";
            }

            BigInteger decryptedKeyBI = RSAUtils.rsaDecrypt(
                    encryptedKeyBI,
                    rsaKeyPair.d,
                    rsaKeyPair.n
            );
            byte[] rawKey = toFixedLength(decryptedKeyBI.toByteArray(), 8);

            byte[] encryptedData = java.util.Base64
                    .getDecoder()
                    .decode(dto.getEncryptedMessage());

            byte[] plainBytes = DESUtils.desDecrypt(rawKey, encryptedData);
            return new String(plainBytes, java.nio.charset.StandardCharsets.UTF_8);

        } catch (Exception ex) {
            return "Error during processing: " + ex.getMessage();
        }
    }

    private byte[] toFixedLength(byte[] arr, int length) {
        if (arr.length == length) {
            return arr;
        } else if (arr.length < length) {
            byte[] tmp = new byte[length];
            System.arraycopy(arr, 0, tmp, length - arr.length, arr.length);
            return tmp;
        } else {

            byte[] tmp = new byte[length];
            System.arraycopy(arr, arr.length - length, tmp, 0, length);
            return tmp;
        }
    }

}
