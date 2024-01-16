package com.delgo.reward.comment.domain;

import com.delgo.reward.comment.controller.request.CommentCreate;
import com.delgo.reward.comment.controller.request.ReplyCreate;
import com.delgo.reward.user.domain.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

    @Test
    void from_comment() {
        // given
        CommentCreate commentCreate = CommentCreate.builder()
                .userId(332)
                .certificationId(1200)
                .content("test comment")
                .build();
        User user = User.builder()
                .userId(332)
                .build();

        // when
        Comment comment = Comment.from(commentCreate, user);

        // then
        assertThat(comment.getUser().getUserId()).isEqualTo(user.getUserId());
        assertThat(comment.getCertificationId()).isEqualTo(commentCreate.certificationId());
        assertThat(comment.getContent()).isEqualTo(commentCreate.content());

    }

    @Test
    void from_reply() {
        // given
        ReplyCreate replyCreate = ReplyCreate.builder()
                .userId(332)
                .certificationId(1200)
                .content("test comment")
                .parentCommentId(111)
                .build();
        User user = User.builder()
                .userId(332)
                .build();

        // when
        Comment reply = Comment.from(replyCreate, user);

        // then
        assertThat(reply.getUser().getUserId()).isEqualTo(user.getUserId());
        assertThat(reply.getCertificationId()).isEqualTo(replyCreate.certificationId());
        assertThat(reply.getContent()).isEqualTo(replyCreate.content());
        assertThat(reply.getIsReply()).isEqualTo(true);
        assertThat(reply.getParentCommentId()).isEqualTo(replyCreate.parentCommentId());
    }
}