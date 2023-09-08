package com.hero.myrestaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hero.myrestaurant.models.Meal;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findBySectionsId(Long sectionId);

    List<Meal> findMealsBySectionsId(Long sectionId);

    List<Meal> findMealsByFoodsId(Long foodId);


}
