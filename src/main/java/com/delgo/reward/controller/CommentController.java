package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.domain.Comment;
import com.delgo.reward.dto.CommentDTO;
import com.delgo.reward.dto.GetCommentDTO;
import com.delgo.reward.dto.ReplyDTO;
import com.delgo.reward.dto.UpdateCommentDTO;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController extends CommController {
    private final CommentService commentService;
    private final CertService certificationService;

    @PostMapping("/comment")
    public ResponseEntity createComment(@Validated @RequestBody CommentDTO commentDTO) throws IOException {
        Comment comment = commentService.createComment(commentDTO);
        certificationService.plusCommentCount(commentDTO.getCertificationId());

        return SuccessReturn(comment);
    }

    @PostMapping("/reply")
    public ResponseEntity createReply(@Validated @RequestBody ReplyDTO replyDTO){
        Comment comment = commentService.createReply(replyDTO);
        return SuccessReturn(comment);
    }

    @GetMapping("/comment")
    public ResponseEntity getComment(@RequestParam int certificationId){
        List<GetCommentDTO> commentList = commentService.getCommentByCertificationId(certificationId);
        return SuccessReturn(commentList);
    }

    @GetMapping("/reply")
    public ResponseEntity getReply(@RequestParam int parentCommentId){
        List<Comment> replyList = commentService.getReplyByParentCommentId(parentCommentId);
        return SuccessReturn(replyList);
    }

    @PutMapping(value = {"/comment/{commentId}", "/comment"})
    public ResponseEntity updateComment(@PathVariable Integer commentId, @RequestBody UpdateCommentDTO updateCommentDTO){
        if(commentService.isCommentOwner(commentId, updateCommentDTO.getUserId())){
            String updateContent = updateCommentDTO.getContent();
            commentService.updateReplyByCommentId(commentId, updateContent);
        }
        else
            return ErrorReturn(ApiCode.INVALID_USER_ERROR);
        return SuccessReturn();
    }

    @DeleteMapping(value = {"/comment/{commentId}/{userId}/{certificationId}", "/comment"})
    public ResponseEntity deleteComment(@PathVariable Integer commentId, @PathVariable Integer userId, @PathVariable Integer certificationId){
        if (commentService.isCommentOwner(commentId, userId) || commentService.isCertificationOwner(commentId, userId)) {
            commentService.deleteReplyByCommentId(commentId);
            certificationService.minusCommentCount(certificationId);
        } else
            return ErrorReturn(ApiCode.INVALID_USER_ERROR);
        return SuccessReturn();
    }
}
