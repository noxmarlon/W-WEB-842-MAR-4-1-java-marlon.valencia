package com.hero.my_restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hero.my_restaurant.models.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
  List<Tutorial> findByPublished(boolean published);
  List<Tutorial> findByTitleContaining(String title);
}