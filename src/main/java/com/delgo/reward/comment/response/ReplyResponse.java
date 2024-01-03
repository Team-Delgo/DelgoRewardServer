package com.delgo.reward.comment.response;

import com.delgo.reward.comment.domain.Comment;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class ReplyResponse extends CommentResponse {
    private final Integer parentCommentId;

    public ReplyResponse(Comment comment) {
        super(comment);
        this.parentCommentId = comment.getParentCommentId();
    }
}
