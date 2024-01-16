package com.delgo.reward.comment.service;

import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.comment.controller.request.CommentCreate;
import com.delgo.reward.comment.controller.request.CommentUpdate;
import com.delgo.reward.comment.controller.request.ReplyCreate;
import com.delgo.reward.comment.domain.Comment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CommentServiceTest {
    @Autowired
    CommentService commentService;

    @Test
    @Transactional
    void create() {
        // given
        CommentCreate commentCreate = CommentCreate.builder()
                .userId(332)
                .certificationId(1200)
                .content("test comment")
                .build();

        // when
        Comment comment = commentService.create(commentCreate);

        // then
        assertThat(comment.getUser().getUserId()).isEqualTo(commentCreate.userId());
        assertThat(comment.getCertificationId()).isEqualTo(commentCreate.certificationId());
        assertThat(comment.getContent()).isEqualTo(commentCreate.content());
    }

    @Test
    @Transactional
    void createReply() {
        // given
        ReplyCreate replyCreate = ReplyCreate.builder()
                .userId(332)
                .certificationId(1200)
                .content("test reply")
                .parentCommentId(615)
                .build();

        // when
        Comment reply = commentService.create(replyCreate);

        // then
        assertThat(reply.getUser().getUserId()).isEqualTo(replyCreate.userId());
        assertThat(reply.getCertificationId()).isEqualTo(replyCreate.certificationId());
        assertThat(reply.getContent()).isEqualTo(replyCreate.content());
        assertThat(reply.getParentCommentId()).isEqualTo(replyCreate.parentCommentId());
    }

    @Test
    void getOneById() {
        // given
        int commentId = 615;

        // when
        Comment comment = commentService.getOneById(commentId);

        // then
        assertThat(comment.getCommentId()).isEqualTo(commentId);
    }

    @Test
    void getListByCertId() {
        // given
        int certId = 2130;

        // when
        List<Comment> commentList = commentService.getListByCertId(certId);

        // then
        assertThat(commentList.size()).isGreaterThan(0);
        assertThat(commentList).extracting(comment -> comment.getCertificationId()).containsOnly(certId);
    }

    @Test
    void getListByParentCommentId() {
        // given
        int parentCommentId = 620;

        // when
        List<Comment> replyList = commentService.getListByParentCommentId(parentCommentId);

        // then
        assertThat(replyList.size()).isGreaterThan(0);
        assertThat(replyList).extracting(comment -> comment.getParentCommentId()).containsOnly(parentCommentId);
        assertThat(replyList).extracting(comment -> comment.getIsReply()).containsOnly(true);
    }

    @Test
    @Transactional
    void update() {
        // given
        CommentUpdate commentUpdate = CommentUpdate.builder()
                .commentId(620)
                .userId(1)
                .content("update test")
                .build();

        // when
        Comment comment = commentService.update(commentUpdate);

        // then
        assertThat(comment.getContent()).isEqualTo(commentUpdate.content());
    }

    @Test
    @Transactional
    void delete() {
        // given
        int commentId = 613;

        // when
        commentService.delete(commentId);

        // then
        assertThatThrownBy(() -> commentService.getOneById(commentId))
                .isInstanceOf(NotFoundDataException.class);
    }
}