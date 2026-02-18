package com.feedbacklens.feedbacklens_backend.feature.feedback;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    public final FeedbackRepository feedbackRepo;

    public FeedbackService(FeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }


    public List<Feedback> getAllFeedbacks() {

        List<Feedback> feedbacks = feedbackRepo.findAll();
        return feedbackRepo.findAll();
    }
}