package com.delgo.reward.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {
    @NotNull
    private Integer userId;
    @NotNull
    private Integer certificationId;
    @NotNull
    private Boolean isReply;
    @NotNull
    private Integer parentCommentId;
    @NotNull
    private String content;
}
