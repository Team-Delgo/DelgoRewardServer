package com.delgo.reward.comment.controller.request;


import javax.validation.constraints.NotNull;

public record ModifyCommentRecord(
        @NotNull Integer commentId,
        @NotNull Integer userId,
        @NotNull String content
) {}