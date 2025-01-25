package com.newsaggregator.service;

import com.newsaggregator.entity.BookmarkedArticle;
import com.newsaggregator.entity.NewsArticle;
import com.newsaggregator.entity.User;
import com.newsaggregator.repository.BookmarkedArticleRepository;
import com.newsaggregator.repository.NewsArticleRepository;
import com.newsaggregator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkedArticleRepository bookmarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewsArticleRepository articleRepository;

    @Transactional
    public BookmarkedArticle addBookmark(Long userId, Long articleId) {
        if (bookmarkRepository.existsByUserIdAndArticleId(userId, articleId)) {
            throw new RuntimeException("Article already bookmarked");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        NewsArticle article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        BookmarkedArticle bookmark = new BookmarkedArticle();
        bookmark.setUser(user);
        bookmark.setArticle(article);

        return bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void removeBookmark(Long userId, Long articleId) {
        bookmarkRepository.deleteByUserIdAndArticleId(userId, articleId);
    }

    public Page<BookmarkedArticle> getUserBookmarks(Long userId, Pageable pageable) {
        return bookmarkRepository.findByUserId(userId, pageable);
    }
}
