package com.delgo.reward.dto;

import com.delgo.reward.domain.user.UserSocial;
import lombok.Data;

@Data
public class OAuthDTO {
    private String socialId = "";
    private String email = "";
    private String phoneNo = "";
    private String gender = "";
    private Integer age;
    private UserSocial userSocial;
}
