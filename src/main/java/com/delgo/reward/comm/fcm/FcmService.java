package com.delgo.reward.comm.fcm;

import com.delgo.reward.user.domain.User;
import com.delgo.reward.service.TokenService;
import com.delgo.reward.user.service.UserQueryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/delgoreward/messages:send";
    private final ObjectMapper objectMapper;
    private final String TITLE = "Delgo";
    private final TokenService tokenService;
    private final UserQueryService userQueryService;

    public void push(int userId, String notifyMsg) {
        try {
            Optional<String> ownerFcmToken = tokenService.getFcmToken(userId);
            User user = userQueryService.getOneByUserId(userId);
            if (user.getIsNotify() && ownerFcmToken.isPresent()) {
                sendMessageTo(ownerFcmToken.get(), notifyMsg);
            }
        } catch (Exception e) {
            log.error("PUSH ERROR userId = {}, notifyMsg = {}", userId, notifyMsg);
            throw new RuntimeException("PUSH ERROR");
        }
    }

    private void sendMessageTo(String targetToken, String body) throws IOException {
        String message = makeMessage(targetToken, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), message);
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        response.close();
    }

    private String makeMessage(String targetToken, String body) throws JsonProcessingException {
        FcmMessageDTO fcmMessage = FcmMessageDTO.builder()
                .message(FcmMessageDTO.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessageDTO.Notification.builder()
                                .title(TITLE)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/delgoreward-firebase-adminsdk-v66yf-a2bdbd5134.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
