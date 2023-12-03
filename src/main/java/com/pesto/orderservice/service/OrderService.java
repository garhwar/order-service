package com.pesto.orderservice.service;

import com.pesto.orderservice.domain.entity.Order;
import com.pesto.orderservice.domain.entity.OrderProduct;
import com.pesto.orderservice.domain.repository.OrderRepository;
import com.pesto.orderservice.web.response.ProductDetails;
import com.pesto.orderservice.web.response.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public Order saveOrder(UserDetails userDetails, List<ProductDetails.Product> productsDetails) {
        Order order = new Order();
        order.setUserId(userDetails.getUserId());
        Set<OrderProduct> orderProducts = new HashSet<>();
        for (ProductDetails.Product productDetails: productsDetails) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setExternalId(productDetails.getProductId());
            orderProducts.add(orderProduct);
        }
        order.setOrderProducts(orderProducts);
        return orderRepository.save(order);
    }
}
