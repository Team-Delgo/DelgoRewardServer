package com.delgo.reward.comment.response;

import com.delgo.reward.comment.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentResponse {
    private final Integer commentId;
    private final Integer certificationId;
    private final String content;
    private final Boolean isReply;

    private final Integer userId;
    private final String userName;
    private final String userProfile; // 기존 profile
    // reply 관련 필드
    private final Integer parentCommentId;

    @JsonFormat(pattern="yyyy.MM.dd/HH:mm/E")
    private final LocalDateTime registDt; // 기존 createDt

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .certificationId(comment.getCertificationId())
                .content(comment.getContent())
                .isReply(comment.getIsReply())
                .userId(comment.getUser().getUserId())
                .userName(comment.getUser().getName())
                .userProfile(comment.getUser().getProfile())
                .registDt(comment.getRegistDt())
                .parentCommentId(comment.getParentCommentId())
                .build();
    }

    public static List<CommentResponse> fromList(List<Comment> commentList){
        return commentList.stream().map(comment -> CommentResponse.from(comment)).toList();
    }
}
