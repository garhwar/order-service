package com.pesto.orderservice.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pesto.orderservice.domain.entity.Order;
import com.pesto.orderservice.service.OrderService;
import com.pesto.orderservice.service.ProductService;
import com.pesto.orderservice.web.requests.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) throws JsonProcessingException {
        // Fetch product details and validate
        productService.validateProductsDetails(
                orderRequest.getOrderProducts().stream().toList());

        // Create and save order
        Order savedOrder = orderService.createOrder(orderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

}
