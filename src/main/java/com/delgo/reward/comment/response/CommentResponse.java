package com.delgo.reward.comment.response;

import com.delgo.reward.comment.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
public class CommentResponse {
    private final Integer commentId;
    private final Integer certificationId;
    private final String content;
    private final Boolean isReply;

    private final Integer userId;
    private final String userName;
    private final String userProfile; // 기존 profile

    @JsonFormat(pattern="yyyy.MM.dd/HH:mm/E")
    private final LocalDateTime registDt; // 기존 createDt

    public CommentResponse(Comment comment) {
        this.commentId = comment.getCommentId();
        this.certificationId = comment.getCertificationId();
        this.content = comment.getContent();
        this.isReply = comment.getIsReply();

        this.userId = comment.getUser().getUserId();
        this.userName = comment.getUser().getName();
        this.userProfile = comment.getUser().getProfile();

        this.registDt = comment.getRegistDt();
    }
}
