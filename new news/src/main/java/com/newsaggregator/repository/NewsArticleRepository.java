package com.newsaggregator.repository;

import com.newsaggregator.entity.NewsArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    // ... your existing methods ...

    @Query("SELECT n FROM NewsArticle n WHERE n.category IN :categories")
    Page<NewsArticle> findByCategories(@Param("categories") Set<String> categories, Pageable pageable);

    @Query("SELECT n FROM NewsArticle n WHERE " +
           "LOWER(n.title) LIKE LOWER(CONCAT('%', :title, '%')) OR " +
           "LOWER(n.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    Page<NewsArticle> findByTitleContainingOrDescriptionContaining(
            @Param("title") String title,
            @Param("description") String description,
            Pageable pageable
    );
}