package com.org.server_service.service;

import com.org.server_service.feign.ClientFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerService {
    private final ClientFeignClient clientFeignClient;

    public String sendResponse(){
        System.out.println("ServerService sendResponse");
        clientFeignClient.sendResponse("ServerService sendResponse");
        return "ServerService sendResponse";
    }

}
