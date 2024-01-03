package com.delgo.reward.comment.domain;

import com.delgo.reward.comment.controller.request.CommentCreate;
import com.delgo.reward.domain.common.BaseTimeEntity;
import com.delgo.reward.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;
    private Integer certificationId;
    private Integer parentCommentId;
    @Setter
    private String content;
    private Boolean isReply;

    @ManyToOne
    @JoinColumn(name = "userId", updatable = false)
    private User user;

    public static Comment from(CommentCreate commentCreate, User user) {
        return Comment.builder()
                .certificationId(commentCreate.certificationId())
                .content(commentCreate.content())
                .isReply(false)
                .parentCommentId(0)
                .user(user)
                .build();
    }

}
