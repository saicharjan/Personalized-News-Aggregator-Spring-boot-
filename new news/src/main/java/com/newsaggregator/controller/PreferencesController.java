package com.newsaggregator.controller;

import com.newsaggregator.entity.User;
import com.newsaggregator.entity.UserPreference;
import com.newsaggregator.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/preferences")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PreferencesController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Set<String>> getUserPreferences() {
        User user = getCurrentUser();
        return ResponseEntity.ok(user.getPreferences().getCategories());
    }

    @PutMapping
    public ResponseEntity<?> updatePreferences(@Valid @RequestBody PreferencesRequest preferencesRequest) {
        User user = getCurrentUser();
        UserPreference userPreference = user.getPreferences();
        if (userPreference == null) {
            userPreference = new UserPreference();
            userPreference.setUser(user);
        }
        userPreference.setCategories(preferencesRequest.getPreferences());
        user.setPreferences(userPreference);
        userRepository.save(user);
        return ResponseEntity.ok("Preferences updated successfully");
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

@Data
class PreferencesRequest {
    private Set<String> preferences;
}