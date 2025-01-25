package com.newsaggregator.service;

import com.newsaggregator.entity.User;
import com.newsaggregator.entity.UserPreference;
import com.newsaggregator.repository.UserPreferenceRepository;
import com.newsaggregator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserPreferenceService {

    @Autowired
    private UserPreferenceRepository preferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserPreference updatePreferences(Long userId, Set<String> categories, Set<String> keywords) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserPreference preference = user.getPreferences();
        if (preference == null) {
            preference = new UserPreference();
            preference.setUser(user);
            user.setPreferences(preference);
        }

        preference.setCategories(categories);
        preference.setKeywords(keywords);

        return preferenceRepository.save(preference);
    }

    public UserPreference getPreferences(Long userId) {
        return preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preferences not found for user"));
    }
}
