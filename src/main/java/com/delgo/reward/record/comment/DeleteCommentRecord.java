package com.delgo.reward.record.comment;


import javax.validation.constraints.NotNull;

public record DeleteCommentRecord(
        @NotNull Integer commentId,
        @NotNull Integer userId,
        @NotNull Integer certificationId
) {
}