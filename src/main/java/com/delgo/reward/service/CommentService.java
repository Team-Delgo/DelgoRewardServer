package com.delgo.reward.service;

import com.delgo.reward.comm.fcm.FcmService;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.Comment;
import com.delgo.reward.domain.notify.NotifyType;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.comment.CommentResDTO;
import com.delgo.reward.record.comment.CommentRecord;
import com.delgo.reward.record.comment.DeleteCommentRecord;
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
     *  유저가 댓글을 작성하면 알림을 저장하고 인증 주인에게 푸시 알림을 보냄
     * @param commentRecord
     * @return 저장된 댓글 데이터 반환
     * @throws IOException
     */
    public Comment createComment(CommentRecord commentRecord) throws IOException {
        User user = userService.getUserById(commentRecord.userId()); // 댓글 작성 유저 조회
        Comment comment = commentRepository.save(commentRecord.toEntity(user)); // 댓글 저장
        Certification certification = certService.getCertById(commentRecord.certificationId()); // 댓글 저장한 인증글 조회

        // commentCount 계산
        int commentCount = commentRepository.countCommentByCertId(commentRecord.certificationId());
        certification.setCommentCount(commentCount);

        // 인증 유저 알림
        String notifyMsg = user.getName() + "님이 나의 게시글에 댓글을 남겼습니다.\n" + commentRecord.content();
        User certOwner = certification.getUser();
        notifyService.saveNotify(certOwner.getUserId(), NotifyType.COMMENT, notifyMsg);
        fcmService.commentPush(certOwner.getUserId(), notifyMsg);

        return comment;
    }

    public Comment getCommentByCommentId(int commentId){
        return commentRepository.findById(commentId).orElseThrow();
    }

    public List<CommentResDTO> getCommentsByCertId(int certificationId){
        return commentRepository.findCommentsByCertId(certificationId)
                .stream().map(CommentResDTO::new).toList();
    }

    public Boolean modifyComment(ModifyCommentRecord modifyCommentRecord) {
        Comment comment = getCommentByCommentId(modifyCommentRecord.userId());
        if (comment.getUser().getUserId() != modifyCommentRecord.userId())  // 유저 체크
            return false;

        comment.setContent(modifyCommentRecord.content());
        return true;
    }

    public Boolean deleteComment(DeleteCommentRecord deleteCommentRecord) {
        // 댓글 OR 인증 작성자인지 CHECK.
        Comment comment = getCommentByCommentId(deleteCommentRecord.commentId());
        Certification certification = certService.getCertById(deleteCommentRecord.certificationId());
        if (comment.getUser().getUserId() != deleteCommentRecord.userId() && certification.getUser().getUserId() != deleteCommentRecord.userId())  // 유저 체크
            return false;

        // Comment 삭제
        commentRepository.deleteById(deleteCommentRecord.commentId());

        // commentCount 업데이트
        int commentCount = commentRepository.countCommentByCertId(deleteCommentRecord.certificationId());
        certification.setCommentCount(commentCount);

        return true;
    }


    // ----------------------------------- Reply -----------------------------------

    /**
     * 유저가 답글을 작성하면 알림을 저장하고 인증 주인과 댓글 주인에게 푸시 알림을 보냄
     * @param replyRecord
     * @return 저장된 답글 데이터 반환
     * @throws IOException
     */
    public Comment createReply(ReplyRecord replyRecord) throws IOException {
        User user = userService.getUserById(replyRecord.userId()); // 답글 작성 유저 조회
        Comment reply = commentRepository.save(replyRecord.toEntity(user));
        Certification certification = certService.getCertById(replyRecord.certificationId()); // 답글 저장한 인증글 조회

        // commentCount 계산
        int commentCount = commentRepository.countCommentByCertId(replyRecord.certificationId());
        certification.setCommentCount(commentCount);

        // 인증 유저 알림
        int certOwnerId = certification.getUser().getUserId();
        String certOwnerNotifyMsg = user.getName() + "님이 나의 게시글에 댓글을 남겼습니다.\n" + replyRecord.content();
        notifyService.saveNotify(certOwnerId, NotifyType.COMMENT, certOwnerNotifyMsg);
        fcmService.commentPush(certOwnerId, certOwnerNotifyMsg);

        // 부모 댓글 유저 알림
        int commentOwnerId = getCommentByCommentId(replyRecord.parentCommentId()).getUser().getUserId();
        String commentOwnerNotifyMsg = user.getName() + "님이 나의 댓글에 답글을 남겼습니다.\n" + replyRecord.content();
        notifyService.saveNotify(commentOwnerId, NotifyType.REPLY, commentOwnerNotifyMsg);
        fcmService.commentPush(commentOwnerId, commentOwnerNotifyMsg);

        return reply;
    }

    public List<CommentResDTO> getReplyByParentCommentId(int parentCommentId){
        return commentRepository.findCommentsByParentCommentId(parentCommentId)
                .stream().map(CommentResDTO::new).toList();
    }
}
