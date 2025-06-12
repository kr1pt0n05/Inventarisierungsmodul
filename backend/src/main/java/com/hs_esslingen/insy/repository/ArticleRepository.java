package com.hs_esslingen.insy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hs_esslingen.insy.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

}
