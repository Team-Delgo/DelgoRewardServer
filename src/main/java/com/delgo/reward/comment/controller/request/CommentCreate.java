package com.delgo.reward.comment.controller.request;

import lombok.Builder;

import javax.validation.constraints.NotNull;


@Builder
public record CommentCreate(
        @NotNull Integer userId,
        @NotNull Integer certificationId,
        @NotNull String content
) {
}



