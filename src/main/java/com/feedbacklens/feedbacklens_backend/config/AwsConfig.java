package com.feedbacklens.feedbacklens_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;

@Configuration
public class AwsConfig {

    @Bean
    public ComprehendClient comprehendClient() {
        return ComprehendClient.builder().region(Region.EU_CENTRAL_1).credentialsProvider(DefaultCredentialsProvider.create()).build();
    }
}
