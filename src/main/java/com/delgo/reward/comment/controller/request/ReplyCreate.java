package com.delgo.reward.comment.controller.request;


import javax.validation.constraints.NotNull;

public record ReplyCreate(
        @NotNull Integer userId,
        @NotNull Integer certificationId,
        @NotNull Integer parentCommentId,
        @NotNull String content
) {
}


