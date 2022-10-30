package com.delgo.reward.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class UserInfoDTO {
    @NotBlank
    private String userName;
    private String profile;
}
