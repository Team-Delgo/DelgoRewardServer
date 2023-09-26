package com.delgo.reward.dto;

import com.delgo.reward.comm.code.UserSocial;
import lombok.Data;

@Data
public class OAuthDTO {
    private String socialId = "";
    private String email = "";
    private String phoneNo = "";
    private String gender; // null 가능 (kakao 정책)
    private Integer age; // null 가능 (kakao 정책)
    private UserSocial userSocial;
}
