package com.hero.myrestaurant.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hero.myrestaurant.exception.ResourceNotFoundException;
import com.hero.myrestaurant.models.Menu;
import com.hero.myrestaurant.models.User;
import com.hero.myrestaurant.repository.MenuRepository;
import com.hero.myrestaurant.repository.UserRepository;
import com.hero.myrestaurant.service.MenuService;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api")
@Validated
public class MenuController {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    MenuService menuService;

    @GetMapping("/menus")
    public ResponseEntity<List<Menu>> getAllMenus(@RequestParam(required = false) String name) {
        List<Menu> menus = new ArrayList<Menu>();

        if (name == null)
            menuRepository.findAll().forEach(menus::add);
        else
            menuRepository.findByNameContaining(name).forEach(menus::add);

        if (menus.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    @GetMapping("/menus/{userId}")
    public ResponseEntity<Menu> getMenuById(@PathVariable("userId") Long userId) {
        Menu menu = menuRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + userId));

        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @PostMapping("/menus/{userId}")
    public ResponseEntity<Menu> createMenu(@PathVariable(value = "userId") Long userId,
            @RequestBody Menu menuRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id = " +
                        userId));
        menuRequest.setUser(user);

        Menu _menu = menuRepository
                .save(menuRequest);

        return new ResponseEntity<>(_menu, HttpStatus.CREATED);
    }

    @PutMapping("/menus/{userId}")
    public ResponseEntity<Menu> updateMenu(@PathVariable("userId") long userId,
            @RequestBody Menu menuRequest) {
        Menu _menu = menuRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Id " + userId + " not found"));

        _menu.setName(menuRequest.getName());

        return new ResponseEntity<>(menuRepository.save(_menu), HttpStatus.OK);
    }
}
