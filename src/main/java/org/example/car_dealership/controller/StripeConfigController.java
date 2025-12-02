package org.example.car_dealership.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripeConfigController {

    @Value("${stripe.pub.key}")
    private String stripePublicKey;

    @GetMapping("/config")
    public Map<String, String> getStripeConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("publishableKey", stripePublicKey);
        return config;
    }
}