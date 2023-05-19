package com.delgo.reward.service;

import com.delgo.reward.comm.fcm.FcmService;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.Comment;
import com.delgo.reward.domain.notify.NotifyType;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.GetCommentDTO;
import com.delgo.reward.record.comment.CommentRecord;
import com.delgo.reward.record.comment.ReplyRecord;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.repository.CommentRepository;
import com.delgo.reward.repository.JDBCTemplateCommentRepository;
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
    private final CertRepository certRepository;
    private final CommentRepository commentRepository;
    private final JDBCTemplateCommentRepository jdbcTemplateCommentRepository;

    /**
     *  유저가 댓글을 작성하면 알림을 저장하고 인증 주인에게 푸시 알림을 보냄
     * @param commentRecord
     * @return 저장된 댓글 데이터 반환
     * @throws IOException
     */
    public Comment createComment(CommentRecord commentRecord) throws IOException {
        Comment comment = commentRepository.save(commentRecord.toEntity()); // 댓글 저장
        Certification certification = certService.getCertById(commentRecord.certificationId()); // 댓글 저장한 인증글 조회

        // commentCount 계산
        int commentCount = commentRepository.countCommentByCertId(commentRecord.certificationId());
        certification.setCommentCount(commentCount);

        String notifyMsg = userService.getUserById(commentRecord.userId()).getName() + "님이 나의 게시글에 댓글을 남겼습니다.\n" + commentRecord.content();
        User owner = certification.getUser();
        notifyService.saveNotify(owner.getUserId(), NotifyType.COMMENT, notifyMsg);
        fcmService.commentPush(owner.getUserId(), notifyMsg);

        return comment;
    }

    /**
     * 유저가 답글을 작성하면 알림을 저장하고 인증 주인과 댓글 주인에게 푸시 알림을 보냄
     * @param replyRecord
     * @return 저장된 답글 데이터 반환
     * @throws IOException
     */
    public Comment createReply(ReplyRecord replyRecord) throws IOException {
        Comment reply = commentRepository.save(replyRecord.toEntity());
        Certification certification = certService.getCertById(replyRecord.certificationId()); // 댓글 저장한 인증글 조회

        // commentCount 계산
        int commentCount = commentRepository.countCommentByCertId(replyRecord.certificationId());
        certification.setCommentCount(commentCount);

        // 인증 유저 알림
        int certOwnerId = certification.getUser().getUserId();
        String certOwnerNotifyMsg = userService.getUserById(replyRecord.userId()).getName() + "님이 나의 게시글에 댓글을 남겼습니다.\n" + replyRecord.content();
        notifyService.saveNotify(certOwnerId, NotifyType.COMMENT, certOwnerNotifyMsg);
        fcmService.commentPush(certOwnerId, certOwnerNotifyMsg);

        // 부모 댓글 유저 알림
        int commentOwnerId = getCommentByCommentId(replyRecord.parentCommentId()).getUserId();
        String commentOwnerNotifyMsg = userService.getUserById(replyRecord.userId()).getName() + "님이 나의 댓글에 답글을 남겼습니다.\n" + replyRecord.content();
        notifyService.saveNotify(commentOwnerId, NotifyType.REPLY, commentOwnerNotifyMsg);
        fcmService.commentPush(commentOwnerId, commentOwnerNotifyMsg);

        return reply;
    }

    public List<GetCommentDTO> getCommentByCertificationId(int certificationId){
        List<GetCommentDTO> getCommentDTOList = jdbcTemplateCommentRepository.findCommentByCertificationId(certificationId);
        return getCommentDTOList;
    }

    public List<Comment> getReplyByParentCommentId(int parentCommentId){
        List<Comment> replyList = commentRepository.findByParentCommentId(parentCommentId);
        return replyList;
    }

    public boolean isCommentOwner(int commentId, int userId){
        Comment comment = getCommentByCommentId(commentId);
        if(comment.getUserId() == userId)
            return true;
        return false;
    }

    public boolean isCertificationOwner(int commentId, int userId){
        Comment comment = getCommentByCommentId(commentId);
        Certification certification = certRepository.findById(comment.getCertificationId()).orElseThrow();

        if(certification.getUser().getUserId() == userId)
            return true;
        return false;
    }


    public Comment getCommentByCommentId(int commentId){
        return commentRepository.findById(commentId).orElseThrow();
    }

    public void updateCommentByCommentId(int commentId, String updateContent){
        commentRepository.updateByCommentId(commentId, updateContent);
    }

    public void deleteCommentByCommentId(int commentId){
        commentRepository.deleteById(commentId);
    }
}
