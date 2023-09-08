package com.hero.myrestaurant.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.hero.myrestaurant.exception.ResourceNotFoundException;
import com.hero.myrestaurant.models.Food;
import com.hero.myrestaurant.models.Meal;
import com.hero.myrestaurant.repository.MealRepository;
import com.hero.myrestaurant.models.Section;
import com.hero.myrestaurant.repository.SectionRepository;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api")
@Validated
public class MealController {
    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @GetMapping("/sections/{sectionId}/meals")
    public ResponseEntity<List<Meal>> getAllMealsBySectionId(@PathVariable(value = "sectionId") Long sectionId) {
        if (!sectionRepository.existsById(sectionId)) {
            throw new ResourceNotFoundException("Not found meal with id = " + sectionId);
        }

        List<Meal> meals = mealRepository.findBySectionsId(sectionId);
        return new ResponseEntity<>(meals, HttpStatus.OK);
    }

    @GetMapping("/meals/{Id}")
    public ResponseEntity<Meal> getMealById(@PathVariable(value = "Id") Long Id) {
        Meal meal = mealRepository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found meal with id = " + Id));

        return new ResponseEntity<>(meal, HttpStatus.OK);
    }

    @PostMapping("/sections/{sectionId}/meals")
    public ResponseEntity<Meal> addMeal(@PathVariable(value = "sectionId") Long sectionId,
            @RequestBody Meal mealRequest) {
        Meal meal = sectionRepository.findById(sectionId).map(section -> {
            section.addMeal(mealRequest);
            return mealRepository.save(mealRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found section with id = " + sectionId));

        return new ResponseEntity<>(meal, HttpStatus.CREATED);
    }

    @PutMapping("/meals/{id}")
    public ResponseEntity<Meal> updateMeal(@PathVariable("id") long id, @RequestBody Meal mealRequest) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal " + id + " not found"));

        meal.setName(mealRequest.getName());
        meal.setPrice(mealRequest.getPrice());

        return new ResponseEntity<>(mealRepository.save(meal), HttpStatus.OK);
    }

    @DeleteMapping("/sections/{sectionId}/meals/{mealId}")
    public ResponseEntity<HttpStatus> deleteMealFromSection(@PathVariable(value = "sectionId") Long sectionId,
            @PathVariable(value = "mealId") Long mealId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found section with id = " + sectionId));

        section.removeMeal(mealId);
        sectionRepository.save(section);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/meals/{id}")
    public ResponseEntity<HttpStatus> deleteMeal(@PathVariable("id") long id) {
        mealRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
