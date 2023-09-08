package com.hero.myrestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hero.myrestaurant.models.RestaurantConfig;

public interface RestaurantConfigRepository extends JpaRepository<RestaurantConfig, Long> {

}
