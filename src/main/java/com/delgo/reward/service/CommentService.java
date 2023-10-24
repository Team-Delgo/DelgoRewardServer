package com.delgo.reward.service;

import com.delgo.reward.certification.controller.port.CertService;
import com.delgo.reward.comm.fcm.FcmService;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.domain.Comment;
import com.delgo.reward.domain.notify.NotifyType;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.comment.CommentResDTO;
import com.delgo.reward.dto.comment.ReplyResDTO;
import com.delgo.reward.record.comment.CommentRecord;
import com.delgo.reward.record.comment.ModifyCommentRecord;
import com.delgo.reward.record.comment.ReplyRecord;
import com.delgo.reward.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    // Service
    private final FcmService fcmService;
    private final CertService certService;
    private final UserService userService;
    private final NotifyService notifyService;

    // Repository
    private final CommentRepository commentRepository;

    /**
     * 댓글에 대한 알림 내용
     */
    public String makeCommentNotifyMsg(String name, String content){
        return name + "님이 나의 게시글에 댓글을 남겼습니다.\n" + content;
    }

    /**
     * 답글에 대한 알림 내용
     */
    public String makeReplyNotifyMsg(String name, String content){
        return name + "님이 나의 댓글에 답글을 남겼습니다.\n" + content;
    }

    /**
     *  유저가 댓글을 작성하면 알림을 저장하고 인증 주인에게 푸시 알림을 보냄
     */
    public CommentResDTO createComment(CommentRecord commentRecord) throws IOException {
        User user = userService.getUserById(commentRecord.userId()); // 댓글 작성 유저 조회
        Comment comment = commentRepository.save(commentRecord.toEntity(user)); // 댓글 저장
        Certification certification = certService.getById(commentRecord.certificationId()); // 댓글 저장한 인증글 조회

        // commentCount 계산
        int commentCount = commentRepository.countCommentByCertId(commentRecord.certificationId());
        certification.setCommentCount(commentCount);

        // 인증 유저 알림
        String notifyMsg = makeCommentNotifyMsg(user.getName(), commentRecord.content());
        User certOwner = certification.getUser();
        notifyService.saveNotify(certOwner.getUserId(), NotifyType.COMMENT, notifyMsg);
        fcmService.commentPush(certOwner.getUserId(), notifyMsg);

        return new CommentResDTO(comment);
    }

    /**
     * [commentId] 뎃글 조회
     */
    public Comment getCommentById(int commentId){
        return commentRepository.findCommentsById(commentId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND Comment ID : " + commentId));
    }

    /**
     * [certificationId] 뎃글 조회
     */
    public List<CommentResDTO> getCommentsByCertId(int certificationId){
        return commentRepository.findCommentsByCertId(certificationId)
                .stream().map(CommentResDTO::new).toList();
    }

    /**
     * 댓글 수정
     */
    public Boolean modifyComment(ModifyCommentRecord modifyCommentRecord) {
        Comment comment = getCommentById(modifyCommentRecord.commentId());
        if (comment.getUser().getUserId() != modifyCommentRecord.userId())  // 유저 체크
            return false;

        comment.setContent(modifyCommentRecord.content());
        return true;
    }

    /**
     * 댓글 삭제
     */
    public Boolean deleteComment(int commentId, int userId, int certificationId) {
        // 댓글 OR 인증 작성자인지 CHECK.
        Comment comment = getCommentById(commentId);
        Certification certification = certService.getById(certificationId);
        if (comment.getUser().getUserId() != userId && certification.getUser().getUserId() != userId)  // 유저 체크
            return false;

        // Comment 삭제
        commentRepository.deleteById(commentId);

        // commentCount 업데이트
        int commentCount = commentRepository.countCommentByCertId(certificationId);
        certification.setCommentCount(commentCount);

        return true;
    }


    // ----------------------------------- Reply -----------------------------------

    /**
     * 유저가 답글을 작성하면 알림을 저장하고 인증 주인과 댓글 주인에게 푸시 알림을 보냄
     */
    public ReplyResDTO createReply(ReplyRecord replyRecord) throws IOException {
        User user = userService.getUserById(replyRecord.userId()); // 답글 작성 유저 조회
        Comment reply = commentRepository.save(replyRecord.toEntity(user));
        Certification certification = certService.getById(replyRecord.certificationId()); // 답글 저장한 인증글 조회

        // commentCount 계산
        int commentCount = commentRepository.countCommentByCertId(replyRecord.certificationId());
        certification.setCommentCount(commentCount);

        // 인증 유저 알림
        int certOwnerId = certification.getUser().getUserId();
        String certOwnerNotifyMsg = makeCommentNotifyMsg(user.getName(), replyRecord.content());
        notifyService.saveNotify(certOwnerId, NotifyType.COMMENT, certOwnerNotifyMsg);
        fcmService.commentPush(certOwnerId, certOwnerNotifyMsg);

        // 부모 댓글 유저 알림
        int commentOwnerId = getCommentById(replyRecord.parentCommentId()).getUser().getUserId();
        String commentOwnerNotifyMsg = makeReplyNotifyMsg(user.getName(), replyRecord.content());
        notifyService.saveNotify(commentOwnerId, NotifyType.REPLY, commentOwnerNotifyMsg);
        fcmService.commentPush(commentOwnerId, commentOwnerNotifyMsg);

        return new ReplyResDTO(reply);
    }

    /**
     * [parentCommentId] 댓글에 대한 답글 조회
     */
    public List<ReplyResDTO> getReplyByParentCommentId(int parentCommentId){
        return commentRepository.findCommentsByParentCommentId(parentCommentId)
                .stream().map(ReplyResDTO::new).toList();
    }
}
