package com.delgo.reward.record.comment;

import com.delgo.reward.domain.Comment;
import com.delgo.reward.user.domain.User;

import javax.validation.constraints.NotNull;

public record ReplyRecord(
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


