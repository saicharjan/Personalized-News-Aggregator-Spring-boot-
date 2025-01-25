package com.newsaggregator.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookmarked_articles")
public class BookmarkedArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private NewsArticle article;

    @Column(name = "bookmarked_at")
    private LocalDateTime bookmarkedAt;

    @PrePersist
    protected void onCreate() {
        bookmarkedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NewsArticle getArticle() {
        return article;
    }

    public void setArticle(NewsArticle article) {
        this.article = article;
    }

    public LocalDateTime getBookmarkedAt() {
        return bookmarkedAt;
    }

    public void setBookmarkedAt(LocalDateTime bookmarkedAt) {
        this.bookmarkedAt = bookmarkedAt;
    }
}
