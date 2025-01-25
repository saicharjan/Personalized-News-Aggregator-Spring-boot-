package com.newsaggregator.service;

import com.newsaggregator.config.NewsApiConfig;
import com.newsaggregator.dto.NewsApiResponse;
import com.newsaggregator.entity.NewsArticle;
import com.newsaggregator.entity.User;
import com.newsaggregator.entity.UserPreference;
import com.newsaggregator.repository.NewsArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsApiService {
    private static final Logger logger = LoggerFactory.getLogger(NewsApiService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Value("${newsapi.key}")
    private String apiKey;

    @Value("${newsapi.baseurl}")
    private String baseUrl;

    @Autowired
    private NewsApiConfig newsApiConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NewsArticleRepository newsArticleRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Cacheable(value = "newsCache", key = "#category")
    public List<NewsArticle> getNewsByCategory(String category) {
        String url = UriComponentsBuilder
                .fromHttpUrl(newsApiConfig.getBaseUrl() + "/top-headlines")
                .queryParam("category", category)
                .queryParam("apiKey", newsApiConfig.getApiKey())
                .toUriString();

        ResponseEntity<NewsApiResponse> response = restTemplate.getForEntity(url, NewsApiResponse.class);
        return convertToNewsArticles(response.getBody());
    }

    public Page<NewsArticle> getPersonalizedNews(UserPreference preferences, Pageable pageable) {
        if (preferences == null || preferences.getCategories().isEmpty()) {
            return newsArticleRepository.findAll(pageable);
        }
        return newsArticleRepository.findByCategories(preferences.getCategories(), pageable);
    }

    public Page<NewsArticle> searchNews(String query, Pageable pageable) {
        return newsArticleRepository.findByTitleContainingOrDescriptionContaining(query, query, pageable);
    }

    public List<NewsArticle> getPersonalizedNews(User user) {
        UserPreference userPreference = user.getPreferences();
        if (userPreference == null || userPreference.getCategories().isEmpty()) {
            return getNewsByCategory("general");
        }

        List<NewsArticle> personalizedNews = new ArrayList<>();
        for (String preference : userPreference.getCategories()) {
            Page<NewsArticle> newsPage = searchNews(preference, Pageable.unpaged());
            personalizedNews.addAll(newsPage.getContent());
        }

        return personalizedNews.stream()
                .sorted(Comparator.comparing(NewsArticle::getPublishedAt).reversed())
                .distinct()
                .limit(50)
                .collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 900000) // 15 minutes
    public void fetchAndUpdateNews() {
        logger.info("Starting scheduled news update");
        for (String category : List.of("business", "technology", "science", "health")) {
            List<NewsArticle> articles = getNewsByCategory(category);
            List<NewsArticle> savedArticles = newsArticleRepository.saveAll(articles);
            
            // Notify subscribers about new articles
            messagingTemplate.convertAndSend("/topic/news/" + category, savedArticles);
        }
        logger.info("Completed scheduled news update");
    }

    private List<NewsArticle> convertToNewsArticles(NewsApiResponse apiResponse) {
        if (apiResponse == null || apiResponse.getArticles() == null) {
            return Collections.emptyList();
        }

        return apiResponse.getArticles().stream()
                .map(article -> {
                    NewsArticle newsArticle = new NewsArticle();
                    newsArticle.setTitle(article.getTitle());
                    newsArticle.setDescription(article.getDescription());
                    newsArticle.setUrl(article.getUrl());
                    newsArticle.setImageUrl(article.getUrlToImage());
                    newsArticle.setSource(article.getSource().getName());
                    
                    try {
                        LocalDateTime publishedAt = LocalDateTime.parse(article.getPublishedAt(), DATE_FORMATTER);
                        newsArticle.setPublishedAt(publishedAt);
                    } catch (Exception e) {
                        newsArticle.setPublishedAt(LocalDateTime.now());
                        logger.warn("Error parsing date: {}", article.getPublishedAt());
                    }

                    return newsArticle;
                })
                .collect(Collectors.toList());
    }
}