package com.delgo.reward.comment.service;

import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.token.service.FcmService;
import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.comment.domain.Comment;
import com.delgo.reward.domain.notify.NotifyType;
import com.delgo.reward.service.NotifyService;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.comment.controller.request.CommentCreate;
import com.delgo.reward.comment.controller.request.CommentUpdate;
import com.delgo.reward.comment.controller.request.ReplyCreate;
import com.delgo.reward.comment.repository.CommentRepository;
import com.delgo.reward.cert.service.CertQueryService;
import com.delgo.reward.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    // Service
    private final FcmService fcmService;
    private final NotifyService notifyService;
    private final CertQueryService certQueryService;
    private final UserQueryService userQueryService;

    // Repository
    private final CommentRepository commentRepository;

    /**
     *  유저가 댓글을 작성하면 알림을 저장하고 인증 주인에게 푸시 알림을 보냄
     */
    public Comment create(CommentCreate commentCreate) throws IOException {
        User user = userQueryService.getOneByUserId(commentCreate.userId()); // 댓글 작성 유저 조회
        Comment comment = commentRepository.save(Comment.from(commentCreate, user)); // 댓글 저장

        // commentCount 계산
        Certification certification = certQueryService.getOneById(commentCreate.certificationId()); // 댓글 저장한 인증글 조회
        int commentCount = commentRepository.countByCertId(commentCreate.certificationId());
        certification.setCommentCount(commentCount);

        // 인증 유저 알림
        String notifyMsg = makeCommentNotifyMsg(user.getName(), commentCreate.content());
        User certOwner = certification.getUser();
        notifyService.saveNotify(certOwner.getUserId(), NotifyType.COMMENT, notifyMsg);
        fcmService.push(certOwner.getUserId(), notifyMsg);

        return comment;
    }

    /**
     * [commentId] 댓글 조회
     */
    public Comment getOneById(int commentId){
        return commentRepository.findOneByCommentId(commentId)
                .orElseThrow(() -> new NotFoundDataException("[Comment] commentId : " + commentId));
    }

    /**
     * [certificationId] 댓글 조회
     */
    public List<Comment> getListByCertId(int certificationId){
        return commentRepository.findListByCertId(certificationId);
    }

    /**
     * 댓글 수정
     */
    public Boolean update(CommentUpdate commentUpdate) {
        Comment comment = getOneById(commentUpdate.commentId());
        if (comment.getUser().getUserId() != commentUpdate.userId())  // 유저 체크
            return false;

        comment.setContent(commentUpdate.content());
        return true;
    }

    /**
     * 댓글 삭제
     */
    public Boolean delete(int commentId, int userId, int certificationId) {
        // 댓글 OR 인증 작성자인지 CHECK.
        Comment comment = getOneById(commentId);
        Certification certification = certQueryService.getOneById(certificationId);
        if (comment.getUser().getUserId() != userId && certification.getUser().getUserId() != userId)  // 유저 체크
            return false;

        // Comment 삭제
        commentRepository.deleteById(commentId);

        // commentCount 업데이트
        int commentCount = commentRepository.countByCertId(certificationId);
        certification.setCommentCount(commentCount);

        return true;
    }


    // ----------------------------------- Reply -----------------------------------

    /**
     * 유저가 답글을 작성하면 알림을 저장하고 인증 주인과 댓글 주인에게 푸시 알림을 보냄
     */
    public Comment createReply(ReplyCreate replyCreate) throws IOException {
        User user = userQueryService.getOneByUserId(replyCreate.userId()); // 답글 작성 유저 조회
        Comment reply = commentRepository.save(replyCreate.toEntity(user));
        Certification certification = certQueryService.getOneById(replyCreate.certificationId()); // 답글 저장한 인증글 조회

        // commentCount 계산
        int commentCount = commentRepository.countByCertId(replyCreate.certificationId());
        certification.setCommentCount(commentCount);

        // 인증 유저 알림
        int certOwnerId = certification.getUser().getUserId();
        String certOwnerNotifyMsg = makeCommentNotifyMsg(user.getName(), replyCreate.content());
        notifyService.saveNotify(certOwnerId, NotifyType.COMMENT, certOwnerNotifyMsg);
        fcmService.push(certOwnerId, certOwnerNotifyMsg);

        // 부모 댓글 유저 알림
        int commentOwnerId = getOneById(replyCreate.parentCommentId()).getUser().getUserId();
        String commentOwnerNotifyMsg = makeReplyNotifyMsg(user.getName(), replyCreate.content());
        notifyService.saveNotify(commentOwnerId, NotifyType.REPLY, commentOwnerNotifyMsg);
        fcmService.push(commentOwnerId, commentOwnerNotifyMsg);

        return reply;
    }

    /**
     * [parentCommentId] 댓글에 대한 답글 조회
     */
    public List<Comment> getListByParentCommentId(int parentCommentId){
        return commentRepository.findListByParentCommentId(parentCommentId);
    }

    public String makeCommentNotifyMsg(String name, String content){
        return name + "님이 나의 게시글에 댓글을 남겼습니다.\n" + content;
    }

    public String makeReplyNotifyMsg(String name, String content){
        return name + "님이 나의 댓글에 답글을 남겼습니다.\n" + content;
    }
}
