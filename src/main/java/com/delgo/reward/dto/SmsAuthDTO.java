package com.delgo.reward.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsAuthDTO {
    @NotNull
    private String phoneNo;
    @NotNull
    private Boolean isJoin;
}
