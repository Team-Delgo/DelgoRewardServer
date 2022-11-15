package com.delgo.reward.comm.fcm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@Data
public class SendFcmDTO {
    @NotNull
    private String targetToken;
    @NotNull
    private String title;
    @NotNull
    private String body;
}
