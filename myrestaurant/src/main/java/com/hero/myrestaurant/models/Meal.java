package com.hero.myrestaurant.models;

import java.util.HashSet;
import java.util.Set;

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

import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties({ "hibernateLazyInitializer" })
@Entity
@Table(name = "meals", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    @Size(max = 20)
    private String price;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, mappedBy = "meals")
    @JsonIgnore
    private Set<Section> sections = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "meal_foods", joinColumns = { @JoinColumn(name = "meal_id") }, inverseJoinColumns = {
            @JoinColumn(name = "food_id") })
    private Set<Food> foods = new HashSet<>();

    public Meal() {
    }

    public Meal(String name, String price, Set<Section> sections, Set<Food> foods) {
        this.name = name;
        this.price = price;
        this.sections = sections;
        this.foods = foods;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public void setSections(Set<Section> sections) {
        this.sections = sections;
    }

    public void addFood(Food food) {
        this.foods.add(food);
        food.getMeals().add(this);
    }

    public Set<Food> getFoods() {
        return foods;
    }

    public void removeFood(long foodId) {
        Food food = this.foods.stream().filter(t -> t.getId() == foodId).findFirst().orElse(null);
        if (food != null) {
            this.foods.remove(food);
            food.getMeals().remove(this);
        }
    }
}
