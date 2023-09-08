package com.hero.myrestaurant.models;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties({ "hibernateLazyInitializer" })
@Entity
@Table(name = "sections", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, mappedBy = "sections")
    @JsonIgnore
    private Set<Menu> menus = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "section_meals", joinColumns = { @JoinColumn(name = "section_id") }, inverseJoinColumns = {
            @JoinColumn(name = "meal_id") })
    private Set<Meal> meals = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "section_foods", joinColumns = { @JoinColumn(name = "section_id") }, inverseJoinColumns = {
            @JoinColumn(name = "food_id") })
    private Set<Food> foods = new HashSet<>();

    public Section() {
    }

    public Section(String name, Set<Menu> menus, Set<Meal> meals, Set<Food> foods) {
        this.name = name;
        this.menus = menus;
        this.meals = meals;
        this.foods = foods;
    }

    public Long getId() {
        return id;
    }

    public @NotBlank @Size(max = 20) String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    public void addMeal(Meal meal) {
        this.meals.add(meal);
        meal.getSections().add(this);
    }

    public Set<Meal> getMeals() {
        return meals;
    }

    public void removeMeal(Long mealId) {
        Meal meal = this.meals.stream().filter(t -> t.getId() == mealId).findFirst().orElse(null);
        if (meal != null) {
            this.meals.remove(meal);
            meal.getSections().remove(this);
        }
    }

    public void addFood(Food food) {
        this.foods.add(food);
        food.getSections().add(this);
    }

    public Set<Food> getFoods() {
        return foods;
    }

    public void removeFood(Long foodId) {
        Food food = this.foods.stream().filter(t -> t.getId() == foodId).findFirst().orElse(null);
        if (food != null) {
            this.foods.remove(food);
            food.getSections().remove(this);
        }
    }
}
