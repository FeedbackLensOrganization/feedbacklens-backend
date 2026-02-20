package com.feedbacklens.feedbacklens_backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SentimentService {

    private final ComprehendClient comprehendClient;

    public Map<String, Object> analyzeTextAwsFormat(String text) throws JsonProcessingException {

        Map<String, Object> result = new HashMap<>();

        // 1️⃣ SENTIMENT
        DetectSentimentRequest sentimentRequest = DetectSentimentRequest.builder().text(text).languageCode("de").build();

        DetectSentimentResponse sentimentResponse = comprehendClient.detectSentiment(sentimentRequest);

        result.put("sentiment", sentimentResponse.sentimentAsString());

        // confidence_json (Großbuchstaben wie AWS Console)
        SentimentScore score = sentimentResponse.sentimentScore();

        Map<String, Float> confidence = new LinkedHashMap<>();
        confidence.put("MIXED", round(score.mixed(), 2));
        confidence.put("NEUTRAL", round(score.neutral(), 2));
        confidence.put("NEGATIVE", round(score.negative(), 2));
        confidence.put("POSITIVE", round(score.positive(), 2));

        result.put("confidence_json", confidence);

        // 2️⃣ KEY PHRASES (nur Text, keine Scores)
        DetectKeyPhrasesRequest keyRequest = DetectKeyPhrasesRequest.builder().text(text).languageCode("de").build();

        DetectKeyPhrasesResponse keyResponse = comprehendClient.detectKeyPhrases(keyRequest);

        List<String> keyPhrasesList = new ArrayList<>();
        for (KeyPhrase phrase : keyResponse.keyPhrases()) {
            keyPhrasesList.add(phrase.text());
        }

        result.put("key_phrases_json", keyPhrasesList);


        return result;
    }

    // Hilfsmethode zum Runden
    private static Float round(Float value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}