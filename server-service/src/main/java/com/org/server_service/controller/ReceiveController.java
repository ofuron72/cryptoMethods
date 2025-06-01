package com.org.server_service.controller;

import com.org.server_service.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReceiveController {

    private final ServerService serverService;


    @PostMapping("/receive")
    public String receive(@RequestBody String pkg) {
        System.out.println("Server receive encrypted package: " + pkg);
        serverService.sendResponse();
        return pkg;
    }
}
