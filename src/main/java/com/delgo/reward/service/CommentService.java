package com.delgo.reward.service;

import com.delgo.reward.comm.fcm.FcmService;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.Comment;
import com.delgo.reward.domain.notify.NotifyType;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.GetCommentDTO;
import com.delgo.reward.dto.ReplyDTO;
import com.delgo.reward.record.comment.CommentRecord;
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
        User user = userService.getUserById(commentRecord.userId()); // 댓글 등록 User 조회
        Comment comment = commentRepository.save(commentRecord.toEntity()); // 댓글 저장
        Certification certification = certService.getCertById(commentRecord.certificationId()); // 댓글 저장한 인증글 조회

        // commentCount 계산
        int commentCount = commentRepository.countCommentByCertId(commentRecord.certificationId());
        certification.setCommentCount(commentCount);

        String notifyMsg = user.getName() + "님이 나의 게시글에 댓글을 남겼습니다.\n" + commentRecord.content();
        User owner = certification.getUser();
        notifyService.saveNotify(owner.getUserId(), NotifyType.COMMENT, notifyMsg);
        fcmService.commentPush(owner.getUserId(), notifyMsg);

        return comment;
    }

    /**
     * 유저가 답글을 작성하면 알림을 저장하고 인증 주인과 댓글 주인에게 푸시 알림을 보냄
     * @param replyDTO
     * @return 저장된 답글 데이터 반환
     * @throws IOException
     */
    public Comment createReply(ReplyDTO replyDTO) throws IOException {
        Comment comment = Comment.builder().isReply(true).certificationId(replyDTO.getCertificationId()).userId(replyDTO.getUserId()).content(replyDTO.getContent()).parentCommentId(replyDTO.getParentCommentId()).build();

        int certUserId = certService.getCertById(replyDTO.getCertificationId()).getUser().getUserId();
        String certUserNotifyMsg = userService.getUserById(replyDTO.getUserId()).getName() + "님이 나의 게시글에 댓글을 남겼습니다.\n" + replyDTO.getContent();

        int commentUserId = getCommentByCommentId(replyDTO.getParentCommentId()).getUserId();
        String commentUserNotifyMsg = userService.getUserById(replyDTO.getUserId()).getName() + "님이 나의 댓글에 답글을 남겼습니다.\n" + replyDTO.getContent();

        notifyService.saveNotify(certUserId, NotifyType.COMMENT, certUserNotifyMsg);
        fcmService.commentPush(certUserId, certUserNotifyMsg);
        notifyService.saveNotify(commentUserId, NotifyType.REPLY, commentUserNotifyMsg);
        fcmService.commentPush(commentUserId, commentUserNotifyMsg);

        return commentRepository.save(comment);
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
