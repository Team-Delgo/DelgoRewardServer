package com.delgo.reward.user.controller.request;

import com.sun.istack.NotNull;
import lombok.*;

@Builder
public record SmsAuthCreate(
        @NotNull @Setter
        String phoneNo,
        @NotNull
        Boolean isJoin
) {
}
