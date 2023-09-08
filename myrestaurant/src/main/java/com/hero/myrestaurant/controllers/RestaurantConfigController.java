package com.hero.myrestaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hero.myrestaurant.exception.ResourceNotFoundException;
import com.hero.myrestaurant.models.RestaurantConfig;
import com.hero.myrestaurant.models.User;
import com.hero.myrestaurant.repository.RestaurantConfigRepository;
import com.hero.myrestaurant.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api")
@Validated
public class RestaurantConfigController {
    @Autowired
    private RestaurantConfigRepository configRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping({ "/config/{userId}" })
    public ResponseEntity<RestaurantConfig> getConfigById(@PathVariable(value = "userId") Long userId) {
        RestaurantConfig config = configRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found config with userId = " + userId));

        return new ResponseEntity<>(config, HttpStatus.OK);
    }

    @PostMapping("/config/{userId}")
    public ResponseEntity<RestaurantConfig> createConfig(@PathVariable(value = "userId") Long userId,
            @RequestBody RestaurantConfig configRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id = " +
                        userId));
        configRequest.setUser(user);

        RestaurantConfig _restaurantConfig = configRepository
                .save(new RestaurantConfig(configRequest.getRestaurantName(),
                        configRequest.getRestaurantUrl(),
                        configRequest.getTheme(), configRequest.getRestaurantAddress(),
                        configRequest.getRestaurantPhone(), configRequest.getRestaurantEmail(),
                        configRequest.getUser()));

        return new ResponseEntity<>(_restaurantConfig, HttpStatus.CREATED);
    }

    @PutMapping("/config/{id}")
    public ResponseEntity<RestaurantConfig> updateConfig(@PathVariable("id") long id,
            @RequestBody RestaurantConfig configRequest) {
        RestaurantConfig _restaurantConfig = configRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id " + id + " not found"));

        _restaurantConfig.setRestaurantName(configRequest.getRestaurantName());
        _restaurantConfig.setRestaurantUrl(configRequest.getRestaurantUrl());
        _restaurantConfig.setTheme(configRequest.getTheme());
        _restaurantConfig.setRestaurantAddress(configRequest.getRestaurantAddress());
        _restaurantConfig.setRestaurantPhone(configRequest.getRestaurantPhone());
        _restaurantConfig.setRestaurantEmail(configRequest.getRestaurantEmail());

        return new ResponseEntity<>(configRepository.save(_restaurantConfig), HttpStatus.OK);
    }

}
