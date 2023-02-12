package com.delgo.reward.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageDTO {
    @NotNull
    private String sender;
    @NotNull
    private String content;
}
