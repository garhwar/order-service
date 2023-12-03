package com.pesto.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pesto.orderservice.domain.entity.Order;
import com.pesto.orderservice.domain.entity.OrderProduct;
import com.pesto.orderservice.domain.repository.OrderRepository;
import com.pesto.orderservice.service.config.KafkaProducerConfig;
import com.pesto.orderservice.web.requests.OrderRequest;
import com.pesto.orderservice.web.response.ProductDetails;
import com.pesto.orderservice.web.response.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    KafkaProducerConfig kafkaProducer;

    public Order createOrder(UserDetails userDetails, List<ProductDetails.Product> productsDetails, OrderRequest orderRequest) throws JsonProcessingException {
        Order order = new Order();
        order.setUserId(userDetails.getUserId());
        order.setStatus("CREATED");
        order.setOrderDate(LocalDate.now());
        Set<OrderProduct> orderProducts = new HashSet<>();
        for (ProductDetails.Product productDetails: productsDetails) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setExternalId(productDetails.getProductId());
            orderProducts.add(orderProduct);
        }
        order.setOrderProducts(orderProducts);
        Order savedOrder =  orderRepository.save(order);
        kafkaProducer.sendOrder(orderRequest.getOrderProducts().stream().toList());
        return savedOrder;
    }
}
