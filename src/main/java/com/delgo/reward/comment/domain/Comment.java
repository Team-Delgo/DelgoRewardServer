package com.delgo.reward.comment.domain;

import com.delgo.reward.domain.common.BaseTimeEntity;
import com.delgo.reward.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;
    private Integer certificationId;
    private Integer parentCommentId;
    private String content;
    private Boolean isReply;

    @ManyToOne
    @JoinColumn(name = "userId", updatable = false)
    private User user;

    public void setContent(String content){
        this.content = content;
    }
}
