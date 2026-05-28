package com.rate.limiter.Service;

import com.rate.limiter.Model.KafkaProducerModel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(KafkaProducerModel userData) {
        kafkaTemplate.send("test-topic",userData.toString());
    }
}
