package com.hero.myrestaurant.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hero.myrestaurant.models.Menu;
import com.hero.myrestaurant.repository.MenuRepository;

@Service
@EnableCaching
public class MenuService {
    @Autowired
    MenuRepository menuRepository;

    @Cacheable("menu")
    public Optional<Menu> findById(Long userId) {
        return menuRepository.findById(userId);
    }
}
