package io.muffin.cartservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSenderService {

    private final KafkaTemplate kafkaTemplate;

    public void sendMessage(Object message) {
        kafkaTemplate.send("test-topic", message);
    }
}
