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

import com.hero.myrestaurant.models.Section;
import com.hero.myrestaurant.repository.SectionRepository;
import com.hero.myrestaurant.repository.MenuRepository;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api")
@Validated
public class SectionController {
    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping("/menus/{menuId}/sections")
    public ResponseEntity<List<Section>> getAllSectionsByMenuId(@PathVariable(value = "menuId") Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Not found menu with id = " + menuId);
        }

        List<Section> sections = sectionRepository.findByMenusId(menuId);
        return new ResponseEntity<>(sections, HttpStatus.OK);
    }

    @GetMapping("/sections/{Id}")
    public ResponseEntity<Section> getSectionById(@PathVariable(value = "Id") Long Id) {
        Section section = sectionRepository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found section with id = " + Id));

        return new ResponseEntity<>(section, HttpStatus.OK);
    }

    @PostMapping("/menus/{menuId}/sections")
    public ResponseEntity<Section> createMenuSection(@PathVariable(value = "menuId") Long menuId,
            @RequestBody Section sectionRequest) {
        Section section = menuRepository.findById(menuId).map(menu -> {
            menu.addSection(sectionRequest);
            return sectionRepository.save(sectionRequest);
            // return sectionRepository.save(new
            // Section(sectionRequest.getName(),sectionRequest.getMeals()
            // ,sectionRequest.getFoods()));
        }).orElseThrow(() -> new ResourceNotFoundException("Not found menu with id = " + menuId));

        return new ResponseEntity<>(section, HttpStatus.CREATED);
    }

    @PutMapping("/sections/{id}")
    public ResponseEntity<Section> updateSection(@PathVariable("id") long id, @RequestBody Section sectionRequest) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section " + id + "not found"));

        section.setName(sectionRequest.getName());

        return new ResponseEntity<>(sectionRepository.save(section), HttpStatus.OK);
    }

    @DeleteMapping("/sections/{id}")
    public ResponseEntity<HttpStatus> deleteSection(@PathVariable("id") long id) {
        sectionRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/menus/{menuId}/sections")
    public ResponseEntity<List<Section>> deleteAllSectionsOfMenu(
            @PathVariable(value = "menuId") Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Not found menu with id = " + menuId);
        }

        sectionRepository.deleteByMenusId(menuId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
