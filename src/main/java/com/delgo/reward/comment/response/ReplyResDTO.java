package com.delgo.reward.comment.response;

import com.delgo.reward.comment.domain.Comment;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class ReplyResDTO extends CommentResDTO {
    private final Integer parentCommentId;

    public ReplyResDTO(Comment comment) {
        super(comment);
        this.parentCommentId = comment.getParentCommentId();
    }
}
