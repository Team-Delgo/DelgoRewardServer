package com.delgo.reward.comm.fcm;

import com.delgo.reward.service.CertService;
import com.delgo.reward.service.TokenService;
import com.fasterxml.jackson.core.JsonParseException;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/delgoreward/messages:send";
    private final ObjectMapper objectMapper;
    private final String TITLE = "DELGO REWARD";
    private final String likePushNotification = "나의 게시물을 좋아하는 이웃 강아지가 있습니다.";
    private final String commentPushNotification = "나의 게시물에 이웃 강아지가 댓글을 남겼습니다.";
    private final TokenService tokenService;

    public void sendMessageTo(String targetToken, String body) throws IOException {
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

        System.out.println(response.body().string());
    }

    public void likePush(int userId) throws IOException {
        String ownerFcmToken = tokenService.getFcmToken(userId);
        sendMessageTo(ownerFcmToken, likePushNotification);
    }

    public void commentPush(int userId) throws IOException {
        String ownerFcmToken = tokenService.getFcmToken(userId);
        sendMessageTo(ownerFcmToken, commentPushNotification);
    }

    private String makeMessage(String targetToken, String body) throws JsonParseException, JsonProcessingException {
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
