package com.delgo.reward.comment.controller.request;

import com.delgo.reward.comment.domain.Comment;
import com.delgo.reward.user.domain.User;

import javax.validation.constraints.NotNull;

public record ReplyCreate(
        @NotNull Integer userId,
        @NotNull Integer certificationId,
        @NotNull Integer parentCommentId,
        @NotNull String content
) {
    public Comment toEntity(User user) {
        return Comment.builder()
                .user(user)
                .certificationId(certificationId)
                .parentCommentId(this.parentCommentId())
                .content(this.content())
                .isReply(true)
                .build();
    }
}


