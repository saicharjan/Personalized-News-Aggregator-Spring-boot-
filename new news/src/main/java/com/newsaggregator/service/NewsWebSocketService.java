package com.newsaggregator.service;

import com.newsaggregator.entity.NewsArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsWebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastNews(List<NewsArticle> news) {
        messagingTemplate.convertAndSend("/topic/news", news);
    }

    public void broadcastNewsUpdate(String category, List<NewsArticle> news) {
        messagingTemplate.convertAndSend("/topic/news/" + category, news);
    }

    public void sendPersonalizedNews(String username, List<NewsArticle> news) {
        messagingTemplate.convertAndSendToUser(
            username,
            "/topic/personalized-news",
            news
        );
    }
}
