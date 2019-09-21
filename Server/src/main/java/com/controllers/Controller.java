package com.controllers;

import com.services.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

@Autowired
ServerService serverService;

    @GetMapping("/")
    public synchronized ResponseEntity clientId (@RequestParam(value = "clientId") String param) {

        int clientId = Integer.parseInt(param.replaceAll("//D+", ""));

        return  serverService.handelRequest(clientId);
    }
}
