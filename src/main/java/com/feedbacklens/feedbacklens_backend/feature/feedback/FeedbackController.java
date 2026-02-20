package com.feedbacklens.feedbacklens_backend.feature.feedback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public ResponseEntity<List<Feedback>> getFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }

    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestBody FeedbackDto request) {
        if (request.getText() == null || request.getText().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Fake Feedback zurückgeben (ID + Text + Zeit)
        Feedback fakeFeedback = new Feedback();
        fakeFeedback.setId(999L); // feste Test-ID


        // Kein DB-Save nötig
        return ResponseEntity.status(HttpStatus.CREATED).body(fakeFeedback);
    }
}
