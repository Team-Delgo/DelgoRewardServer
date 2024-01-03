package com.delgo.reward.record.comment;

import com.delgo.reward.domain.Comment;
import com.delgo.reward.user.domain.User;

import javax.validation.constraints.NotNull;

public record CommentRecord(
        @NotNull Integer userId,
        @NotNull Integer certificationId,
        @NotNull String content
) {
    public Comment toEntity(User user) {
        return Comment.builder()
                .user(user)
                .certificationId(certificationId)
                .content(this.content())
                .isReply(false)
                .build();
    }
}



