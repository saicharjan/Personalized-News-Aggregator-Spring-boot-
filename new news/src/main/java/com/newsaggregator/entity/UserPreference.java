package com.newsaggregator.entity;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "user_preferences")
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection
    @CollectionTable(name = "user_categories", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "category")
    private Set<String> categories = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_keywords", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "keyword")
    private Set<String> keywords = new HashSet<>();

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

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }
}
