package com.newsaggregator.repository;

import com.newsaggregator.entity.BookmarkedArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkedArticleRepository extends JpaRepository<BookmarkedArticle, Long> {
    Page<BookmarkedArticle> findByUserId(Long userId, Pageable pageable);
    boolean existsByUserIdAndArticleId(Long userId, Long articleId);
    void deleteByUserIdAndArticleId(Long userId, Long articleId);
}
