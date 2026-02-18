package com.feedbacklens.feedbacklens_backend.feature.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
