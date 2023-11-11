package com.delgo.reward.user.controller.response;

import com.delgo.reward.user.domain.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmTokenResponse {
    @Schema(description = "유저 아이디")
    protected Integer userId;
    @Schema(description = "유저 FCM 토큰")
    protected String fcmToken;

    public static FcmTokenResponse from(Token token){
        return FcmTokenResponse
                .builder()
                .userId(token.getUserId())
                .fcmToken(token.getFcmToken())
                .build();
    }
}
