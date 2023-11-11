package com.delgo.reward.user.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsAuthResponse {
    @Schema(description = "유저 휴대폰 번호")
    protected String phoneNo;
    @Schema(description = "가입 여부")
    protected Boolean isJoin;
}
