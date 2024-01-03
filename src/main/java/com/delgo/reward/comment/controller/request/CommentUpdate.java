package com.delgo.reward.comment.controller.request;


import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record CommentUpdate (
        @NotNull Integer commentId,
        @NotNull Integer userId,
        @NotNull String content
) {
}