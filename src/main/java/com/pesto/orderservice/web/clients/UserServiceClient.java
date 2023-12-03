package com.pesto.orderservice.web.clients;

import com.pesto.orderservice.web.response.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private final String userServiceUrl;

    @Autowired
    public UserServiceClient(RestTemplate restTemplate, @Value("${user.service.url}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }

    public UserDetails getUserByExternalId(Long userId) {
        String url = userServiceUrl + "/api/v1/users/" + userId;
        ResponseEntity<UserDetails> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                UserDetails.class
        );
        return responseEntity.getBody();
    }
}
