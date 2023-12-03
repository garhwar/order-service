package com.pesto.orderservice.web.clients;

import com.pesto.orderservice.web.response.ProductDetails;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class ProductServiceClient {

    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    @Autowired
    public ProductServiceClient(RestTemplate restTemplate, @Value("${product.service.url}") String productServiceUrl) {
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
    }

    public ProductDetails getProductByExternalId(Long productId) {
        String url = productServiceUrl + "/api/v1/products/" + productId;
        ResponseEntity<ProductDetails> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                ProductDetails.class
        );
        return responseEntity.getBody();
    }

    public ProductDetails getProductsByExternalIds(List<Long> productIds) {
        String url = productServiceUrl + "/api/v1/products";
        ResponseEntity<ProductDetails> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                Collections.singletonMap("ids", productIds)
        );
        return responseEntity.getBody();
    }
}
