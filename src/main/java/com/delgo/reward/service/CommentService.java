package com.delgo.reward.service;

import com.delgo.reward.domain.Certification;
import com.delgo.reward.domain.Comment;
import com.delgo.reward.dto.CommentDTO;
import com.delgo.reward.dto.GetCommentDTO;
import com.delgo.reward.dto.ReplyDTO;
import com.delgo.reward.repository.CertificationRepository;
import com.delgo.reward.repository.CommentRepository;
import com.delgo.reward.repository.JDBCTemplateCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CertificationRepository certificationRepository;
    private final JDBCTemplateCommentRepository jdbcTemplateCommentRepository;

    public Comment createComment(CommentDTO commentDTO){
        Comment comment = Comment.builder().isReply(false).certificationId(commentDTO.getCertificationId()).userId(commentDTO.getUserId()).content(commentDTO.getContent()).build();
        return commentRepository.save(comment);
    }

    public Comment createReply(ReplyDTO replyDTO){
        Comment comment = Comment.builder().isReply(true).certificationId(replyDTO.getCertificationId()).userId(replyDTO.getUserId()).content(replyDTO.getContent()).parentCommentId(replyDTO.getParentCommentId()).build();
        return commentRepository.save(comment);
    }

    public List<GetCommentDTO> getCommentByCertificationId(int certificationId){
//        List<Comment> commentList = commentRepository.findByCertificationId(certificationId);
        List<GetCommentDTO> getCommentDTOList = jdbcTemplateCommentRepository.findCommentByCertificationId(certificationId);
//        return commentList;
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
        Certification certification = certificationRepository.findByCertificationId(comment.getCertificationId()).orElseThrow();

        if(certification.getUserId() == userId)
            return true;
        return false;
    }


    public Comment getCommentByCommentId(int commentId){
        return commentRepository.findById(commentId).orElseThrow();
    }

    public void updateReplyByCommentId(int commentId, String updateContent){
        commentRepository.updateByCommentId(commentId, updateContent);
    }

    public void deleteReplyByCommentId(int commentId){
        commentRepository.deleteById(commentId);
    }
}
