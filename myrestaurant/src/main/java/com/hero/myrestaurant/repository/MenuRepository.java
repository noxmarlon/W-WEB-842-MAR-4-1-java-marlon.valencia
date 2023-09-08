package com.hero.myrestaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hero.myrestaurant.models.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByNameContaining(String name);

    Menu getMenuById(Long userId);
}
