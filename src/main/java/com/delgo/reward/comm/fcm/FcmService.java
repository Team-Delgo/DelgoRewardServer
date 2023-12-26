package com.delgo.reward.comm.fcm;

import com.delgo.reward.domain.user.User;
import com.delgo.reward.service.TokenService;
import com.delgo.reward.service.user.UserQueryService;
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
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/delgoreward/messages:send";
    private final ObjectMapper objectMapper;
    private final String TITLE = "Delgo";
    private final String likePushNotification = "나의 게시물을 좋아하는 이웃 강아지가 있습니다.";
    private final String commentPushNotification = "나의 게시물에 이웃 강아지가 댓글을 남겼습니다.";
    private final TokenService tokenService;
    private final UserQueryService userQueryService;

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
        response.close();
    }

    public boolean checkNotify(int userId){
        User user = userQueryService.getUserById(userId);
        return user.getIsNotify();
    }

    public void likePush(int userId, String likeMsg) throws IOException {
        Optional<String> ownerFcmToken = tokenService.getFcmToken(userId);
        if(checkNotify(userId) && ownerFcmToken.isPresent()){
            sendMessageTo(ownerFcmToken.get(), likeMsg);
        }
        else
            return ;
    }

    public void commentPush(int certOwnerId, String notifyMsg) throws IOException {
        Optional<String> ownerFcmToken = tokenService.getFcmToken(certOwnerId);
        if(checkNotify(certOwnerId) && ownerFcmToken.isPresent()){
            sendMessageTo(ownerFcmToken.get(), notifyMsg);
        }
        else
            return ;
    }

    public void birthdayPush(Map<Integer, String> userIdAndPetNameMap, String notifyMsg) {
        List<Integer> userIdList = userIdAndPetNameMap.keySet().stream().toList();
        Map<Integer, String> userFcmTokenMap = tokenService.getFcmToken(userIdList);

        userFcmTokenMap.forEach((userId, fcmToken) -> {
            if(checkNotify(userId) && !fcmToken.isEmpty()){
                try {
                    sendMessageTo(fcmToken, userIdAndPetNameMap.get(userId) + notifyMsg);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

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
