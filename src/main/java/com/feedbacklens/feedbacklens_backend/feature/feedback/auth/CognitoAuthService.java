package com.feedbacklens.feedbacklens_backend.feature.feedback.auth;

import com.feedbacklens.feedbacklens_backend.config.cognito.AuthResponse;
import com.feedbacklens.feedbacklens_backend.config.cognito.CognitoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CognitoAuthService {

    private final CognitoIdentityProviderClient cognitoClient;
    private final CognitoProperties props;

    public AuthResponse login(String username, String password) {
        InitiateAuthRequest request = InitiateAuthRequest.builder()
                .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .clientId(props.getClientId())
                .authParameters(Map.of(
                        "USERNAME", username,
                        "PASSWORD", password
                ))
                .build();

        InitiateAuthResponse response = cognitoClient.initiateAuth(request);

        // Challenge abfangen (z.B. NEW_PASSWORD_REQUIRED beim ersten Login)
        if (response.challengeName() != null) {
            if (response.challengeName() == ChallengeNameType.NEW_PASSWORD_REQUIRED) {
                // Passwort direkt als neues Passwort setzen (einmalig)
                return handleNewPasswordChallenge(response.session(), username, password);
            }
            throw new RuntimeException("Unbekannte Challenge: " + response.challengeName());
        }

        AuthenticationResultType result = response.authenticationResult();
        return new AuthResponse(
                result.accessToken(),
                result.idToken(),
                result.refreshToken(),
                result.expiresIn()
        );
    }

    private AuthResponse handleNewPasswordChallenge(String session, String username, String newPassword) {
        RespondToAuthChallengeRequest challengeRequest = RespondToAuthChallengeRequest.builder()
                .clientId(props.getClientId())
                .challengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                .session(session)
                .challengeResponses(Map.of(
                        "USERNAME", username,
                        "NEW_PASSWORD", newPassword
                ))
                .build();

        RespondToAuthChallengeResponse challengeResponse = cognitoClient.respondToAuthChallenge(challengeRequest);
        AuthenticationResultType result = challengeResponse.authenticationResult();

        return new AuthResponse(
                result.accessToken(),
                result.idToken(),
                result.refreshToken(),
                result.expiresIn()
        );
    }
}