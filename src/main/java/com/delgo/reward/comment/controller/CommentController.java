package com.delgo.reward.comment.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.comment.controller.request.CommentRecord;
import com.delgo.reward.comment.controller.request.ModifyCommentRecord;
import com.delgo.reward.comment.controller.request.ReplyRecord;
import com.delgo.reward.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Hidden
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController extends CommController {

    private final CommentService commentService;

    /**
     * 댓글 생성
     * @param commentRecord
     * @return 생성된 댓글 데이터
     * @throws IOException
     */
    @PostMapping("/comment")
    public ResponseEntity createComment(@Validated @RequestBody CommentRecord commentRecord) throws IOException {
        return SuccessReturn(commentService.createComment(commentRecord));
    }

    /**
     * 인증에 대한 댓글 조회
     * @param certificationId
     * @return 댓글 데이터
     */
    @GetMapping("/comment")
    public ResponseEntity getComment(@RequestParam int certificationId){
        return SuccessReturn(commentService.getCommentsByCertId(certificationId));
    }

    /**
     * 댓글 / 답글 수정
     * @param modifyCommentRecord
     * @return 수정된 댓글 데이터
     */
    @PutMapping("/comment")
    public ResponseEntity updateComment(@Validated @RequestBody ModifyCommentRecord modifyCommentRecord){
        return commentService.modifyComment(modifyCommentRecord)
                ? SuccessReturn()
                : ErrorReturn(APICode.INVALID_USER_ERROR);
    }

    /**
     * 댓글 / 답글 삭제
     * @return 성공 / 실패 여부
     */
    @DeleteMapping("/comment/{commentId}/{userId}/{certificationId}")
    public ResponseEntity deleteComment(@PathVariable Integer commentId, @PathVariable Integer userId, @PathVariable Integer certificationId) {
        return commentService.deleteComment(commentId, userId, certificationId)
                ? SuccessReturn()
                : ErrorReturn(APICode.INVALID_USER_ERROR);
    }

    /**
     * 답글 생성
     * @param replyRecord
     * @return 생성된 답글 데이터
     * @throws IOException
     */
    @PostMapping("/reply")
    public ResponseEntity createReply(@Validated @RequestBody ReplyRecord replyRecord) throws IOException{
        return SuccessReturn(commentService.createReply(replyRecord));
    }

    /**
     * 댓글에 대한 답글 조회
     * @param parentCommentId
     * @return 답글 데이터
     */
    @GetMapping("/reply")
    public ResponseEntity getReply(@RequestParam int parentCommentId){
        return SuccessReturn(commentService.getReplyByParentCommentId(parentCommentId));
    }

}
