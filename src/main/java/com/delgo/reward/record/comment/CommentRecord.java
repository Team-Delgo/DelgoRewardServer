package com.delgo.reward.record.comment;

import com.delgo.reward.domain.Comment;

import javax.validation.constraints.NotNull;

public record CommentRecord(
        @NotNull Integer userId,
        @NotNull Integer certificationId,
        @NotNull Boolean isReply,
        @NotNull String content
) {
    public Comment toEntity() {
        return Comment.builder()
                .userId(this.userId())
                .certificationId(this.certificationId())
                .isReply(this.isReply())
                .content(this.content())
                .build();
    }
}



