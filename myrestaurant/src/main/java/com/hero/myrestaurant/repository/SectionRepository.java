package com.hero.myrestaurant.repository;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hero.myrestaurant.models.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByMenusId(Long menuId);

    @Transactional
    void deleteByMenusId(Long menuId);
}
