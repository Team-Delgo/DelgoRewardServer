package com.delgo.reward.token.service;

import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.service.CertQueryService;
import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.notify.domain.Notify;
import com.delgo.reward.notify.service.NotifyService;
import com.delgo.reward.token.domain.*;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.service.UserQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    private final TokenService tokenService;
    private final NotifyService notifyService;
    private final UserQueryService userQueryService;
    private final CertQueryService certQueryService;

    public void pushByComment(int sendUserId, int receiveUserId, int certificationId) {
        try {
            Token token = tokenService.getOneByUserId(receiveUserId);
            User recipient = userQueryService.getOneByUserId(receiveUserId);

            if (!recipient.getIsNotify() || StringUtils.isEmpty(token.getFcmToken())) return;

            User sender = userQueryService.getOneByUserId(sendUserId);
            Certification certification = certQueryService.getOneById(certificationId);
            Notify notify = notifyService.save(recipient.getUserId(), NotifyType.Comment, NotifyType.Comment.getBody());

            String title = NotifyType.Comment.getTitle();
            String body = "["+ sender.getName() +"]" + NotifyType.Comment.getBody();
            String url = NotifyType.Comment.getUrl() + certificationId;
            String image = certification.getPhotos().get(0);

            FcmMessage message = FcmMessage.from(
                    token.getFcmToken(),
                    FcmData.from(NotifyType.Comment, title, body, url, image), // 안드로이드
                    FcmIOS.from(NotifyType.Comment, title, url, image, notify.getNotifyId()) // 애플
            );

            sendMessageTo(message);

        } catch (Exception e) {
            log.error("[FCM] Comment receiveUserId = {}, certificationId = {} ", receiveUserId, certificationId);
            throw new RuntimeException("PUSH ERROR");
        }
    }

    public void pushByReaction(int sendUserId, int receiveUserId,int certificationId, ReactionCode reactionCode) {
        try {
            Token token = tokenService.getOneByUserId(receiveUserId);
            User recipient = userQueryService.getOneByUserId(receiveUserId);
            if (!recipient.getIsNotify() || StringUtils.isEmpty(token.getFcmToken())) return;

            User sender = userQueryService.getOneByUserId(sendUserId);
            Certification certification = certQueryService.getOneById(certificationId);
            Notify notify = notifyService.save(recipient.getUserId(), NotifyType.Comment, NotifyType.Reaction.getBody());

            String title = reactionCode.getPushTitle();
            String body = "["+ sender.getName() +"] " + reactionCode.getPushBody();
            String url = NotifyType.Reaction.getUrl() + certificationId;
            String image = certification.getPhotos().get(0);

            FcmMessage message = FcmMessage.from(
                    token.getFcmToken(),
                    FcmData.from(NotifyType.Reaction, title, body, url, image), // 안드로이드
                    FcmIOS.from(NotifyType.Reaction, title, url, image, notify.getNotifyId()) // 애플
            );

            sendMessageTo(message);

        } catch (Exception e) {
            log.error("[FCM] Comment receiveUserId = {}, certificationId = {} ", receiveUserId, certificationId);
            throw new RuntimeException("PUSH ERROR");
        }
    }

    private void sendMessageTo(FcmMessage fcmMessage) throws IOException {
        String API_URL = "https://fcm.googleapis.com/v1/projects/delgoreward/messages:send";

        String json = new ObjectMapper().writeValueAsString(
                Map.of("validateOnly", false,
                        "message", fcmMessage));

        System.out.println("json = " + json);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        assert response.body() != null;
        System.out.println("response = " + response.body().string());
        response.close();
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
