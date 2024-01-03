package com.delgo.reward.comment.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.comment.controller.request.CommentCreate;
import com.delgo.reward.comment.controller.request.CommentUpdate;
import com.delgo.reward.comment.controller.request.ReplyCreate;
import com.delgo.reward.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController extends CommController {
    private final CommentService commentService;

    /**
     * 댓글 생성
     */
    @PostMapping("/comment")
    public ResponseEntity createComment(@Validated @RequestBody CommentCreate commentCreate) throws IOException {
        return SuccessReturn(commentService.createComment(commentCreate));
    }

    /**
     * 인증에 대한 댓글 조회
     */
    @GetMapping("/comment")
    public ResponseEntity getComment(@RequestParam int certificationId){
        return SuccessReturn(commentService.getCommentsByCertId(certificationId));
    }

    /**
     * 댓글 / 답글 수정
     */
    @PutMapping("/comment")
    public ResponseEntity updateComment(@Validated @RequestBody CommentUpdate commentUpdate){
        return commentService.modifyComment(commentUpdate)
                ? SuccessReturn()
                : ErrorReturn(APICode.INVALID_USER_ERROR);
    }

    /**
     * 댓글 / 답글 삭제
     */
    @DeleteMapping("/comment/{commentId}/{userId}/{certificationId}")
    public ResponseEntity deleteComment(@PathVariable Integer commentId, @PathVariable Integer userId, @PathVariable Integer certificationId) {
        return commentService.deleteComment(commentId, userId, certificationId)
                ? SuccessReturn()
                : ErrorReturn(APICode.INVALID_USER_ERROR);
    }

    /**
     * 답글 생성
     */
    @PostMapping("/reply")
    public ResponseEntity createReply(@Validated @RequestBody ReplyCreate replyCreate) throws IOException{
        return SuccessReturn(commentService.createReply(replyCreate));
    }

    /**
     * 댓글에 대한 답글 조회
     */
    @GetMapping("/reply")
    public ResponseEntity getReply(@RequestParam int parentCommentId){
        return SuccessReturn(commentService.getReplyByParentCommentId(parentCommentId));
    }

}
