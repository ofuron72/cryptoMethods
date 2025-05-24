package com.org.client_service;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.security.Security;

@EnableFeignClients
@SpringBootApplication
public class ClientServiceApplication {
	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		SpringApplication.run(ClientServiceApplication.class, args);
	}
}
