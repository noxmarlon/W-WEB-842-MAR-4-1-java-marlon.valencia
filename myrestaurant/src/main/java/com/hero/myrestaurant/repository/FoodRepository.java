package com.hero.myrestaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hero.myrestaurant.models.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByMealsId(Long mealId);

    List<Food> findBySectionsId(Long sectionId);

    List<Food> findByNameContaining(String name);
}
