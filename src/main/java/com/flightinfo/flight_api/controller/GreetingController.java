package com.flightinfo.flight_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (A simple controller to verify the API is running).
 */
@RestController
public class GreetingController {

    /**
     * (This method will handle GET requests to the /api/v1/hello URL).
     */
    @GetMapping("/api/v1/hello")
    public String sayHello() {
        return "Hello, World! Welcome to the Flight API.";
    }

}