package com.delgo.reward.comment.controller.request;

import com.delgo.reward.comment.domain.Comment;
import com.delgo.reward.user.domain.User;
import lombok.Builder;

import javax.validation.constraints.NotNull;


@Builder
public record CommentCreate(
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



