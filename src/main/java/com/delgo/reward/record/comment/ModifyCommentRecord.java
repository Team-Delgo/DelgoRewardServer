package com.delgo.reward.record.comment;


import javax.validation.constraints.NotNull;

public record ModifyCommentRecord(
        @NotNull Integer commentId,
        @NotNull Integer userId,
        @NotNull String content
) {}