package com.delgo.reward.user.controller.request;

import com.sun.istack.NotNull;
import lombok.*;

@Getter
public class SmsAuthCreate {
    @NotNull @Setter
    private String phoneNo;
    @NotNull
    private Boolean isJoin;
}
