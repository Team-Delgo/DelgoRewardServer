package com.delgo.reward.user.controller.response;

import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.user.domain.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthResponse {
    private String socialId = "";
    private String email = "";
    private String phoneNo = "";
    private String gender; // null 가능 (kakao 정책)
    private Integer age; // null 가능 (kakao 정책)
    private UserSocial userSocial;

    public static OAuthResponse from(User user){
        return OAuthResponse
                .builder()
                .build();
    }
}
