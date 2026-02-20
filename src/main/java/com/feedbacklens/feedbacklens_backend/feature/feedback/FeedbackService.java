package com.feedbacklens.feedbacklens_backend.feature.feedback;

import com.feedbacklens.feedbacklens_backend.services.SentimentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FeedbackService {
    public final FeedbackRepository feedbackRepo;
    private final SentimentService sentimentService;

    public FeedbackService(FeedbackRepository feedbackRepo, SentimentService sentimentService) {
        this.feedbackRepo = feedbackRepo;
        this.sentimentService = sentimentService;
    }


    public List<Feedback> getAllFeedbacks() {
        return feedbackRepo.findAll();
    }

    public Feedback analyzeTextInAWSComprehend(String text) throws Exception {
        Map<String, Object> analysis = sentimentService.analyzeTextAwsFormat(text);
        Feedback feedback = new Feedback();
        feedback.setText(text);
        feedback.setSentiment((String) analysis.get("sentiment"));
        feedback.setConfidenceJson((Map<String, Object>) analysis.get("confidence_json"));
        feedback.setKeyPhrasesJson((List<String>) analysis.get("key_phrases_json"));

        return feedbackRepo.save(feedback);

    }
}