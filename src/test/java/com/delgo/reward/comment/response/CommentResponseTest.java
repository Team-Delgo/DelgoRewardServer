package com.delgo.reward.comment.response;

import com.delgo.reward.comment.domain.Comment;
import com.delgo.reward.comment.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CommentResponseTest {
    @Autowired
    CommentService commentService;

    @Test
    void from() {
        // given
        Comment comment = commentService.getOneById(613);

        // when
        CommentResponse commentResponse = CommentResponse.from(comment);

        // then
        assertThat(commentResponse.getCommentId()).isEqualTo(comment.getCommentId());
        assertThat(commentResponse.getUserId()).isEqualTo(comment.getUser().getUserId());
        assertThat(commentResponse.getCertificationId()).isEqualTo(comment.getCertificationId());
        assertThat(commentResponse.getContent()).isEqualTo(comment.getContent());
        assertThat(commentResponse.getIsReply()).isEqualTo(comment.getIsReply());
        assertThat(commentResponse.getParentCommentId()).isEqualTo(comment.getParentCommentId());
        assertThat(commentResponse.getUserName()).isEqualTo(comment.getUser().getName());
        assertThat(commentResponse.getUserProfile()).isEqualTo(comment.getUser().getProfile());
        assertThat(commentResponse.getRegistDt()).isEqualTo(comment.getRegistDt());
    }

    @Test
    void fromList() {
        // given
        Comment comment1 = commentService.getOneById(613);
        Comment comment2 = commentService.getOneById(614);
        List<Comment> commentList = List.of(comment1, comment2);

        // when
        List<CommentResponse> commentResponseList = CommentResponse.fromList(commentList);

        // then
        assertThat(commentResponseList.get(0).getCommentId()).isEqualTo(comment1.getCommentId());
        assertThat(commentResponseList.get(0).getUserId()).isEqualTo(comment1.getUser().getUserId());
        assertThat(commentResponseList.get(0).getCertificationId()).isEqualTo(comment1.getCertificationId());
        assertThat(commentResponseList.get(0).getContent()).isEqualTo(comment1.getContent());
        assertThat(commentResponseList.get(0).getIsReply()).isEqualTo(comment1.getIsReply());
        assertThat(commentResponseList.get(0).getParentCommentId()).isEqualTo(comment1.getParentCommentId());
        assertThat(commentResponseList.get(0).getUserName()).isEqualTo(comment1.getUser().getName());
        assertThat(commentResponseList.get(0).getUserProfile()).isEqualTo(comment1.getUser().getProfile());
        assertThat(commentResponseList.get(0).getRegistDt()).isEqualTo(comment1.getRegistDt());

        assertThat(commentResponseList.get(1).getCommentId()).isEqualTo(comment2.getCommentId());
        assertThat(commentResponseList.get(1).getUserId()).isEqualTo(comment2.getUser().getUserId());
        assertThat(commentResponseList.get(1).getCertificationId()).isEqualTo(comment2.getCertificationId());
        assertThat(commentResponseList.get(1).getContent()).isEqualTo(comment2.getContent());
        assertThat(commentResponseList.get(1).getIsReply()).isEqualTo(comment2.getIsReply());
        assertThat(commentResponseList.get(1).getParentCommentId()).isEqualTo(comment2.getParentCommentId());
        assertThat(commentResponseList.get(1).getUserName()).isEqualTo(comment2.getUser().getName());
        assertThat(commentResponseList.get(1).getUserProfile()).isEqualTo(comment2.getUser().getProfile());
        assertThat(commentResponseList.get(1).getRegistDt()).isEqualTo(comment2.getRegistDt());
    }
}