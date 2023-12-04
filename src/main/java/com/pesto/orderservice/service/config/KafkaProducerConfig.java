package com.pesto.orderservice.service.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pesto.orderservice.web.requests.OrderMessage;
import com.pesto.orderservice.web.requests.OrderRequest;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerConfig {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaProducerConfig(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrder(Long orderId, OrderRequest orderRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setUserId(orderRequest.getUserId());
        orderMessage.setOrderId(orderId);
        orderMessage.setOrderProducts(orderRequest.getOrderProducts());
        String message = objectMapper.writeValueAsString(orderMessage);
        ProducerRecord<String, Object> record = new ProducerRecord<>("order-topic", message);
        kafkaTemplate.send(record);
    }
}
