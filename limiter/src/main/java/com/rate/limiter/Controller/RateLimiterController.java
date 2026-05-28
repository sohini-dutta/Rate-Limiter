package com.rate.limiter.Controller;

import com.rate.limiter.Model.KafkaProducerModel;
import com.rate.limiter.Service.KafkaProducer;
import com.rate.limiter.Service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api")
public class RateLimiterController {
    private final RateLimiterService rateLimiterService;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final KafkaProducer producerService;
    public RateLimiterController(RateLimiterService rateLimiterService, KafkaProducer producerService) {
        this.rateLimiterService = rateLimiterService;
        this.producerService = producerService;
    }
    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(@RequestHeader("X-CLIENT-ID") String clientId, HttpServletRequest servletRequest) throws Exception {
        String endPoint = servletRequest.getRequestURI();
        boolean allowed = rateLimiterService.isAllowed(clientId);
        if (!allowed) {
            KafkaProducerModel userData = new KafkaProducerModel(clientId, "User not allowed", endPoint);
            producerService.sendMessage(userData);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://jsonplaceholder.typicode.com/posts/1"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        KafkaProducerModel userData = new KafkaProducerModel(clientId, "User allowed", endPoint);
        producerService.sendMessage(userData);
        return ResponseEntity.ok(response.body());
    }
}