package com.pesto.orderservice.service;

import com.pesto.orderservice.web.clients.ProductServiceClient;
import com.pesto.orderservice.web.requests.OrderRequest;
import com.pesto.orderservice.web.response.ProductDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    ProductServiceClient productServiceClient;
    public ProductDetails getProductDetails(Long externalId) {
        return productServiceClient.getProductByExternalId(externalId);
    }

    public List<ProductDetails.Product> getProductsDetails(List<OrderRequest.OrderProduct> orderProducts) {
        Map<Long, Integer> orderRequestMap = orderProducts.stream().collect(Collectors.toMap(
                OrderRequest.OrderProduct::getProductId, OrderRequest.OrderProduct::getQuantity
        ));
        List<Long> externalIds = orderRequestMap.keySet().stream().toList();
        ProductDetails returnedProducts = productServiceClient.getProductsByExternalIds(externalIds);
        Map<Long, Integer> returnedProductsMap = returnedProducts.getContent().stream().collect(Collectors.toMap(
                ProductDetails.Product::getProductId, ProductDetails.Product::getQuantityAvailable
        ));
        if (returnedProducts.getContent().size() != orderRequestMap.size())
            throw new IllegalStateException("Products in order not found in product service");
        for (Long externalId: orderRequestMap.keySet())
            if (orderRequestMap.get(externalId) > returnedProductsMap.get(externalId))
                throw new IllegalStateException("Product is out of stock");

        return returnedProducts.getContent();
    }
}
