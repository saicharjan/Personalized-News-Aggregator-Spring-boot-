package com.newsaggregator.controller;

import com.newsaggregator.entity.NewsArticle;
import com.newsaggregator.entity.User;
import com.newsaggregator.repository.UserRepository;
import com.newsaggregator.service.NewsApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NewsController {

    @Autowired
    private NewsApiService newsApiService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/feed")
    public ResponseEntity<List<NewsArticle>> getPersonalizedNewsFeed() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(newsApiService.getPersonalizedNews(user));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<NewsArticle>> getNewsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(newsApiService.getNewsByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<NewsArticle>> searchNews(
            @RequestParam String query,
            Pageable pageable) {
        return ResponseEntity.ok(newsApiService.searchNews(query, pageable));
    }

    @GetMapping("/public/top")
    public ResponseEntity<List<NewsArticle>> getTopNews() {
        return ResponseEntity.ok(newsApiService.getNewsByCategory("general"));
    }
}