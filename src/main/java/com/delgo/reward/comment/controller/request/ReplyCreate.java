package com.delgo.reward.comment.controller.request;


import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record ReplyCreate(
        @NotNull Integer userId,
        @NotNull Integer certificationId,
        @NotNull Integer parentCommentId,
        @NotNull String content
) {
}