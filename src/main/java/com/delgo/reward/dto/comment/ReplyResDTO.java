package com.delgo.reward.dto.comment;

import com.delgo.reward.domain.Comment;
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
