package com.delgo.reward.push.service;

import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.service.CertQueryService;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.mungple.service.MungpleService;
import com.delgo.reward.push.domain.FcmMessage;
import com.delgo.reward.push.domain.NotifyType;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.service.UserQueryService;
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


@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    private final NotifyService notifyService;
    private final MungpleService mungpleService;
    private final UserQueryService userQueryService;
    private final CertQueryService certQueryService;

    public void comment(int sendUserId,int receiveUserId, int certificationId) {
        try {
            if(sendUserId == receiveUserId) return;
            if(!userQueryService.checkNotificationPermission(receiveUserId)) return;

            User sender = userQueryService.getOneByUserId(sendUserId);
            User receiver = userQueryService.getOneByUserId(receiveUserId);
            Certification certification = certQueryService.getOneById(certificationId);

            FcmMessage message = FcmMessage.cert(NotifyType.Comment, receiver.getFcmToken(), sender.getName(), certification);
            sendMessageTo(message);

            notifyService.create(receiver.getUserId(), message.getData().getBody(), NotifyType.Comment);
        } catch (Exception e) {
            log.error("[FCM] ERROR : {}", e.getMessage());
            throw new RuntimeException("PUSH ERROR");
        }
    }

    public void helper(int sendUserId, int receiveUserId, int certificationId) {
        try {
            if(sendUserId == receiveUserId) return;
            if(!userQueryService.checkNotificationPermission(receiveUserId)) return;

            User sender = userQueryService.getOneByUserId(sendUserId);
            User receiver = userQueryService.getOneByUserId(receiveUserId);
            Certification certification = certQueryService.getOneById(certificationId);

            FcmMessage message = FcmMessage.cert(NotifyType.Helper, receiver.getFcmToken(), sender.getName(), certification);
            sendMessageTo(message);

            notifyService.create(receiver.getUserId(), message.getData().getBody(), NotifyType.Helper);
        } catch (Exception e) {
            log.error("[FCM] ERROR : {}", e.getMessage());
            throw new RuntimeException("PUSH ERROR");
        }
    }

    public void cute(int sendUserId, int receiveUserId, int certificationId) {
        try {
            if(sendUserId == receiveUserId) return;
            if(!userQueryService.checkNotificationPermission(receiveUserId)) return;

            User sender = userQueryService.getOneByUserId(sendUserId);
            User receiver = userQueryService.getOneByUserId(receiveUserId);
            Certification certification = certQueryService.getOneById(certificationId);

            FcmMessage message = FcmMessage.cert(NotifyType.Cute, receiver.getFcmToken(), sender.getName(), certification);
            sendMessageTo(message);

            notifyService.create(receiver.getUserId(), message.getData().getBody(), NotifyType.Cute);
        } catch (Exception e) {
            log.error("[FCM] ERROR : {}", e.getMessage());
            throw new RuntimeException("PUSH ERROR");
        }
    }

    public void mungple(int receiveUserId, int mungpleId) {
        try {
            if(!userQueryService.checkNotificationPermission(receiveUserId)) return;

            User receiver = userQueryService.getOneByUserId(receiveUserId);
            Mungple mungple = mungpleService.getOneByMungpleId(mungpleId);

            FcmMessage message = FcmMessage.mungple(NotifyType.Mungple, receiver.getFcmToken(), receiver.getPetName(), mungple);
            sendMessageTo(message);

            notifyService.create(receiver.getUserId(), message.getData().getBody(), NotifyType.Mungple);
        } catch (Exception e) {
            log.error("[FCM] ERROR : {}", e.getMessage());
            throw new RuntimeException("PUSH ERROR");
        }
    }

    public void mungpleByMe(int receiveUserId, int mungpleId) {
        try {
            if(!userQueryService.checkNotificationPermission(receiveUserId)) return;

            User receiver = userQueryService.getOneByUserId(receiveUserId);
            Mungple mungple = mungpleService.getOneByMungpleId(mungpleId);

            FcmMessage message = FcmMessage.mungple(NotifyType.MungpleByMe, receiver.getFcmToken(), receiver.getPetName(), mungple);
            sendMessageTo(message);

            notifyService.create(receiver.getUserId(), message.getData().getBody(), NotifyType.MungpleByMe);
        } catch (Exception e) {
            log.error("[FCM] ERROR : {}", e.getMessage());
            throw new RuntimeException("PUSH ERROR");
        }
    }

    public void mungpleByOther(int firstFounderId, int receiveUserId, int mungpleId) {
        try {
            if(!userQueryService.checkNotificationPermission(receiveUserId)) return;

            User firstFounder = userQueryService.getOneByUserId(firstFounderId);
            User receiver = userQueryService.getOneByUserId(receiveUserId);
            Mungple mungple = mungpleService.getOneByMungpleId(mungpleId);

            FcmMessage message = FcmMessage.mungple(NotifyType.MungpleByOther, receiver.getFcmToken(), firstFounder.getPetName(), mungple);
            sendMessageTo(message);

            notifyService.create(receiver.getUserId(), message.getData().getBody(), NotifyType.MungpleByOther);
        } catch (Exception e) {
            log.error("[FCM] ERROR : {}", e.getMessage());
            throw new RuntimeException("PUSH ERROR");
        }
    }

    public void sendMessageTo(FcmMessage fcmMessage) {
        try {
            String API_URL = "https://fcm.googleapis.com/v1/projects/delgoreward/messages:send";
            String requestJson = new ObjectMapper().writeValueAsString(
                    Map.of("validateOnly", false,
                            "message", fcmMessage));

            System.out.println(requestJson);

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestJson);
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
        } catch (Exception e) {
            log.error("[FCM] ERROR : {}", e.getMessage());
            throw new RuntimeException("PUSH ERROR");
        }
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
