CREATE TABLE feedback
(
    id               BIGSERIAL PRIMARY KEY,              -- Unique identifier, auto-incrementing
    text             TEXT        NOT NULL,               -- The feedback content
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(), -- Timestamp with time zone, defaults to current time
    sentiment        VARCHAR(50),                        -- Sentiment label (e.g., 'positive', 'negative', 'neutral')
    confidence_json  JSONB,                              -- Confidence scores or metadata in JSON format
    key_phrases_json JSONB                               -- Extracted key phrases in JSON format
);