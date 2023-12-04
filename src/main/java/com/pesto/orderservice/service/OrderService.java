package com.pesto.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pesto.orderservice.domain.entity.Order;
import com.pesto.orderservice.domain.entity.OrderProduct;
import com.pesto.orderservice.domain.repository.OrderRepository;
import com.pesto.orderservice.service.config.KafkaProducerConfig;
import com.pesto.orderservice.web.requests.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    KafkaProducerConfig kafkaProducer;

    public Order createOrder(OrderRequest orderRequest) throws JsonProcessingException {
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setStatus("CREATED");
        order.setOrderDate(LocalDate.now());
        Set<OrderProduct> orderProducts = new HashSet<>();
        for (OrderRequest.OrderProduct productDetails: orderRequest.getOrderProducts()) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setExternalId(productDetails.getProductId());
            orderProduct.setQuantity(productDetails.getQuantity());
            orderProducts.add(orderProduct);
        }
        order.setOrderProducts(orderProducts);
        Order savedOrder = orderRepository.save(order);
        kafkaProducer.sendOrder(savedOrder.getId(), orderRequest);
        return savedOrder;
    }
}
