package com.delgo.reward.comment.controller;

import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.service.CertQueryService;
import com.delgo.reward.common.controller.CommController;
import com.delgo.reward.comm.code.APICode;
import com.delgo.reward.comment.controller.request.CommentCreate;
import com.delgo.reward.comment.controller.request.CommentUpdate;
import com.delgo.reward.comment.controller.request.ReplyCreate;
import com.delgo.reward.comment.domain.Comment;
import com.delgo.reward.comment.response.CommentResponse;
import com.delgo.reward.comment.service.CommentService;
import com.delgo.reward.comm.push.service.FcmService;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController extends CommController {
    private final FcmService fcmService;
    private final CommentService commentService;
    private final UserQueryService userQueryService;
    private final CertQueryService certQueryService;

    /**
     * 댓글 생성
     */
    @PostMapping("/comment")
    public ResponseEntity createComment(@Validated @RequestBody CommentCreate commentCreate) {
        Comment comment = commentService.create(commentCreate);

        // push
        Certification certification = certQueryService.getOneById(commentCreate.certificationId());
        fcmService.comment(commentCreate.userId(), certification.getUserId(), certification.getCertificationId());
        return SuccessReturn(CommentResponse.from(comment));
    }

    /**
     * 인증에 대한 댓글 조회
     */
    @GetMapping("/comment")
    public ResponseEntity getComment(@RequestParam int certificationId){
        List<Comment> commentList = commentService.getListByCertId(certificationId);
        return SuccessReturn(CommentResponse.fromList(commentList));
    }

    /**
     * 댓글 / 답글 수정
     */
    @PutMapping("/comment")
    public ResponseEntity updateComment(@Validated @RequestBody CommentUpdate commentUpdate){
        Comment comment = commentService.getOneById(commentUpdate.commentId());
        User user = userQueryService.getOneByUserId(commentUpdate.userId());

        return (comment.getUser().getUserId() != user.getUserId()) // INVALID USER CHECK
                ? ErrorReturn(APICode.INVALID_USER_ERROR)
                : SuccessReturn(commentService.update(commentUpdate));
    }

    /**
     * 댓글 / 답글 삭제
     */
    @DeleteMapping("/comment/{commentId}/{userId}/{certificationId}")
    public ResponseEntity deleteComment(@PathVariable Integer commentId, @PathVariable Integer userId, @PathVariable Integer certificationId) {
        Comment comment = commentService.getOneById(commentId);
        Certification certification = certQueryService.getOneById(certificationId);
        if (comment.getUser().getUserId() != userId && certification.getUser().getUserId() != userId)  // 유저 체크
            return ErrorReturn(APICode.INVALID_USER_ERROR);

        commentService.delete(commentId);
        return SuccessReturn();
    }

    /**
     * 답글 생성
     */
    @PostMapping("/reply")
    public ResponseEntity createReply(@Validated @RequestBody ReplyCreate replyCreate) {
        Comment reply = commentService.create(replyCreate);

        // 인증 유저 알림
        Certification certification = certQueryService.getOneById(replyCreate.certificationId()); // 답글 저장한 인증글 조회
        fcmService.comment(replyCreate.userId(), certification.getUserId() ,replyCreate.certificationId());

        // 부모 댓글 유저 알림
        int commentOwnerId = commentService.getOneById(replyCreate.parentCommentId()).getUser().getUserId();
        fcmService.comment(replyCreate.userId(), commentOwnerId, replyCreate.certificationId());
        return SuccessReturn(CommentResponse.from(reply));
    }

    /**
     * 댓글에 대한 답글 조회
     */
    @GetMapping("/reply")
    public ResponseEntity getReply(@RequestParam int parentCommentId){
        List<Comment> replyList = commentService.getListByParentCommentId(parentCommentId);
        return SuccessReturn(CommentResponse.fromList(replyList));
    }

}
