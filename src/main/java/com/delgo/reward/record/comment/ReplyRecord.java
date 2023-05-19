package com.delgo.reward.record.comment;

import com.delgo.reward.domain.Comment;

import javax.validation.constraints.NotNull;

public record ReplyRecord(
        @NotNull Integer userId,
        @NotNull Integer certificationId,
        @NotNull Integer parentCommentId,
        @NotNull String content
) {
    public Comment toEntity() {
        return Comment.builder()
                .userId(this.userId())
                .certificationId(this.certificationId())
                .parentCommentId(this.parentCommentId())
                .content(this.content())
                .isReply(true)
                .build();
    }
}


