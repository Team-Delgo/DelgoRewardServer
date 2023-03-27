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
@RequestMapping("/api")
public class CommentController extends CommController {
    private final CommentService commentService;
    private final CertService certificationService;

    /**
     * 댓글 생성
     * @param commentDTO
     * @return 생성된 댓글 데이터
     * @throws IOException
     */
    @PostMapping("/comment")
    public ResponseEntity createComment(@Validated @RequestBody CommentDTO commentDTO) throws IOException {
        Comment comment = commentService.createComment(commentDTO);
        certificationService.plusCommentCount(commentDTO.getCertificationId());

        return SuccessReturn(comment);
    }

    /**
     * 답글 생성
     * @param replyDTO
     * @return 생성된 답글 데이터
     * @throws IOException
     */
    @PostMapping("/reply")
    public ResponseEntity createReply(@Validated @RequestBody ReplyDTO replyDTO) throws IOException{
        Comment comment = commentService.createReply(replyDTO);
        return SuccessReturn(comment);
    }

    /**
     * 인증에 대한 댓글 조회
     * @param certificationId
     * @return 댓글 데이터
     */
    @GetMapping("/comment")
    public ResponseEntity getComment(@RequestParam int certificationId){
        List<GetCommentDTO> commentList = commentService.getCommentByCertificationId(certificationId);
        return SuccessReturn(commentList);
    }

    /**
     * 댓글에 대한 답글 조회
     * @param parentCommentId
     * @return 답글 데이터
     */
    @GetMapping("/reply")
    public ResponseEntity getReply(@RequestParam int parentCommentId){
        List<Comment> replyList = commentService.getReplyByParentCommentId(parentCommentId);
        return SuccessReturn(replyList);
    }

    /**
     * 댓글 / 답글 수정
     * @param commentId
     * @param updateCommentDTO
     * @return 수정된 댓글 데이터
     */
    @PutMapping(value = {"/comment/{commentId}", "/comment"})
    public ResponseEntity updateComment(@PathVariable Integer commentId, @RequestBody UpdateCommentDTO updateCommentDTO){
        if(commentService.isCommentOwner(commentId, updateCommentDTO.getUserId())){
            String updateContent = updateCommentDTO.getContent();
            commentService.updateCommentByCommentId(commentId, updateContent);
        }
        else
            return ErrorReturn(ApiCode.INVALID_USER_ERROR);
        return SuccessReturn();
    }

    /**
     * 댓글 / 답글 삭제
     * @param commentId
     * @param userId
     * @param certificationId
     * @return 성공 / 실패 여부
     */
    @DeleteMapping(value = {"/comment/{commentId}/{userId}/{certificationId}", "/comment"})
    public ResponseEntity deleteComment(@PathVariable Integer commentId, @PathVariable Integer userId, @PathVariable Integer certificationId){
        if (commentService.isCommentOwner(commentId, userId) || commentService.isCertificationOwner(commentId, userId)) {
            commentService.deleteCommentByCommentId(commentId);
            certificationService.minusCommentCount(certificationId);
        } else
            return ErrorReturn(ApiCode.INVALID_USER_ERROR);
        return SuccessReturn();
    }
}
