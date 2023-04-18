package com.delgo.reward.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FcmTokenDTO {
    private Integer userId;
    private String fcmToken;
}
