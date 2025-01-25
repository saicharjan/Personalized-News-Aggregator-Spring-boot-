package com.newsaggregator.controller;

import com.newsaggregator.entity.NewsArticle;
import com.newsaggregator.entity.User;
import com.newsaggregator.service.NewsApiService;
import com.newsaggregator.service.NewsWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class NewsWebSocketController {

    @Autowired
    private NewsApiService newsApiService;

    @Autowired
    private NewsWebSocketService newsWebSocketService;

    @MessageMapping("/subscribe")
    public void subscribeToNews(SimpMessageHeaderAccessor headerAccessor) {
        Principal user = headerAccessor.getUser();
        if (user != null) {
            List<NewsArticle> news = newsApiService.getNewsByCategory("general");
            newsWebSocketService.sendPersonalizedNews(user.getName(), news);
        }
    }

    @MessageMapping("/subscribe/category")
    public void subscribeToCategory(@Payload String category, SimpMessageHeaderAccessor headerAccessor) {
        List<NewsArticle> news = newsApiService.getNewsByCategory(category);
        newsWebSocketService.broadcastNewsUpdate(category, news);
    }

    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void broadcastNewsUpdates() {
        List<NewsArticle> latestNews = newsApiService.getNewsByCategory("general");
        newsWebSocketService.broadcastNews(latestNews);
    }
}
