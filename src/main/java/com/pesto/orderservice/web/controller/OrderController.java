package com.pesto.orderservice.web.controller;

import com.pesto.orderservice.domain.entity.Order;
import com.pesto.orderservice.service.OrderService;
import com.pesto.orderservice.service.ProductService;
import com.pesto.orderservice.service.UserService;
import com.pesto.orderservice.web.requests.OrderRequest;
import com.pesto.orderservice.web.response.ProductDetails;
import com.pesto.orderservice.web.response.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        // Fetch user details and validate
//        UserDetails userDetails = userService.getUserDetails(orderRequest.getUserId());
        UserDetails userDetails = new UserDetails();

        // Fetch product details and validate
        List<ProductDetails.Product> returnedProducts = productService.getProductsDetails(
                orderRequest.getOrderProducts().stream().toList());

        // Create and save order
        Order savedOrder = orderService.saveOrder(userDetails, returnedProducts);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

}
