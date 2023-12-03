package com.pesto.orderservice.service;

import com.pesto.orderservice.web.clients.UserServiceClient;
import com.pesto.orderservice.web.response.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserServiceClient userServiceClient;

    public UserDetails getUserDetails(Long userId) {
        return userServiceClient.getUserByExternalId(userId);
    }
}
