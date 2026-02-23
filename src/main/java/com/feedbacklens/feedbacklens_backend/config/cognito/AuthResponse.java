package com.feedbacklens.feedbacklens_backend.config.cognito;

public record AuthResponse(
        String accessToken,
        String idToken,
        String refreshToken,
        Integer expiresIn
) {}