package com.feedbacklens.feedbacklens_backend.feature.feedback;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    private String sentiment;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "confidence_json", columnDefinition = "jsonb")
    private Map<String, Object> confidenceJson;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "key_phrases_json", columnDefinition = "jsonb")
    private List<String> keyPhrasesJson;
}