package com.hero.myrestaurant.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hero.myrestaurant.exception.ResourceNotFoundException;

import com.hero.myrestaurant.models.Food;
import com.hero.myrestaurant.repository.FoodRepository;
import com.hero.myrestaurant.models.Section;
import com.hero.myrestaurant.repository.SectionRepository;
import com.hero.myrestaurant.service.FoodService;
import com.hero.myrestaurant.models.Meal;
import com.hero.myrestaurant.repository.MealRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
@Validated
public class FoodController {
    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    FoodService foodService;

    @GetMapping("/foods")
    public ResponseEntity<List<Food>> getAllFoods(@RequestParam(required = false) String name) {
        List<Food> foods = new ArrayList<Food>();

        if (name == null)
            foodRepository.findAll().forEach(foods::add);
        else
            foodRepository.findByNameContaining(name).forEach(foods::add);

        if (foods.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @GetMapping("/foods/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable("id") long id) {
        Food food = foodService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found food with id = " + id));

        return new ResponseEntity<>(food, HttpStatus.OK);
    }

    @GetMapping("/sections/{sectionId}/foods")
    public ResponseEntity<List<Food>> getAllFoodsBySectionId(@PathVariable(value = "sectionId") Long sectionId) {
        if (!sectionRepository.existsById(sectionId)) {
            throw new ResourceNotFoundException("Not found section with id = " + sectionId);
        }

        List<Food> foods = foodRepository.findBySectionsId(sectionId);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @GetMapping("/meals/{mealId}/foods")
    public ResponseEntity<List<Food>> getAllFoodsByMealsId(@PathVariable(value = "mealId") Long mealId) {
        if (!mealRepository.existsById(mealId)) {
            throw new ResourceNotFoundException("Not found meal with id = " + mealId);
        }

        List<Food> foods = foodRepository.findByMealsId(mealId);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @PostMapping("/sections/{sectionId}/foods")
    public ResponseEntity<Food> createSectionFood(@PathVariable(value = "sectionId") Long sectionId,
            @RequestBody Food foodRequest) {

        Food food = sectionRepository.findById(sectionId).map(section -> {
            section.addFood(foodRequest);
            return foodRepository.save(foodRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found section with id = " + sectionId));

        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @PostMapping("/meals/{mealId}/foods")
    public ResponseEntity<Food> createMealFood(@PathVariable(value = "mealId") Long mealId,
            @RequestBody Food foodRequest) {

        Food food = mealRepository.findById(mealId).map(meal -> {
            meal.addFood(foodRequest);
            return foodRepository.save(foodRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found meal with id = " + mealId));

        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @PutMapping("/foods/{id}")
    public ResponseEntity<Food> updateFood(@PathVariable("id") long id, @RequestBody Food foodRequest) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food " + id + " not found"));

        food.setName(foodRequest.getName());
        food.setPrice(foodRequest.getPrice());
        food.setDescription(foodRequest.getDescription());
        food.setAllergen(foodRequest.getAllergen());
        if (foodRequest.getActive() == false)
            food.setActive(foodRequest.getActive());

        return new ResponseEntity<>(foodRepository.save(food), HttpStatus.OK);
    }

    @DeleteMapping("/meals/{mealId}/foods/{foodId}")
    public ResponseEntity<HttpStatus> deleteFoodFromMeal(@PathVariable(value = "mealId") Long mealId,
            @PathVariable(value = "foodId") Long foodId) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found meal with id = " + mealId));

        meal.removeFood(foodId);
        mealRepository.save(meal);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/sections/{sectionId}/foods/{foodId}")
    public ResponseEntity<HttpStatus> deleteFoodFromSection(@PathVariable(value = "sectionId") Long sectionId,
            @PathVariable(value = "foodId") Long foodId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found section with id = " + sectionId));

        section.removeFood(foodId);
        sectionRepository.save(section);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/foods/{id}")
    public ResponseEntity<HttpStatus> deleteFood(@PathVariable("id") long id) {
        foodRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
