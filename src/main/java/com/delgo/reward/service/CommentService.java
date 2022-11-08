package com.delgo.reward.service;

import com.delgo.reward.domain.Comment;
import com.delgo.reward.dto.CommentDTO;
import com.delgo.reward.dto.ReplyDTO;
import com.delgo.reward.repository.CommentRepository;
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

    public Comment createComment(CommentDTO commentDTO){
        Comment comment = Comment.builder().isReply(false).certificationId(commentDTO.getCertificationId()).userId(commentDTO.getUserId()).content(commentDTO.getContent()).build();
        return commentRepository.save(comment);
    }

    public Comment createReply(ReplyDTO replyDTO){
        Comment comment = Comment.builder().isReply(true).certificationId(replyDTO.getCertificationId()).userId(replyDTO.getUserId()).content(replyDTO.getContent()).parentCommentId(replyDTO.getParentCommentId()).build();
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentByCertificationId(int certificationId){
        List<Comment> commentList = commentRepository.findByCertificationId(certificationId);
        return commentList;
    }

    public List<Comment> getReplyByParentCommentId(int parentCommentId){
        List<Comment> replyList = commentRepository.findByParentCommentId(parentCommentId);
        return replyList;
    }

    public void deleteReplyByCommentId(int commentId){
        commentRepository.deleteById(commentId);
    }
}
