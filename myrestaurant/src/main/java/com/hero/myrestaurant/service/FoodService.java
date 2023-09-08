package com.hero.myrestaurant.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hero.myrestaurant.models.Food;
import com.hero.myrestaurant.repository.FoodRepository;

@Service
@EnableCaching
public class FoodService {
    @Autowired
    FoodRepository foodRepository;

    @Cacheable("food")
    public Optional<Food> findById(long id) {
        doLongRunningTask();

        return foodRepository.findById(id);
    }

    private void doLongRunningTask() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
