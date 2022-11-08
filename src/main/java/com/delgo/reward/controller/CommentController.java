package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.Comment;
import com.delgo.reward.dto.CommentDTO;
import com.delgo.reward.dto.ReplyDTO;
import com.delgo.reward.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController extends CommController {
    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity createComment(@Validated @RequestBody CommentDTO commentDTO){
        Comment comment = commentService.createComment(commentDTO);
        return SuccessReturn(comment);
    }

    @PostMapping("/reply")
    public ResponseEntity createReply(@Validated @RequestBody ReplyDTO replyDTO){
        Comment comment = commentService.createReply(replyDTO);
        return SuccessReturn(comment);
    }

    @GetMapping("/comment")
    public ResponseEntity getComment(@RequestParam int certificationId){
        List<Comment> commentList = commentService.getCommentByCertificationId(certificationId);
        return SuccessReturn(commentList);
    }

    @GetMapping("/reply")
    public ResponseEntity getReply(@RequestParam int parentCommentId){
        List<Comment> replyList = commentService.getReplyByParentCommentId(parentCommentId);
        return SuccessReturn(replyList);
    }

    @GetMapping("/comment/delete")
    public ResponseEntity deleteReply(@RequestParam int commentId){
        commentService.deleteReplyByCommentId(commentId);
        return SuccessReturn();
    }
}
