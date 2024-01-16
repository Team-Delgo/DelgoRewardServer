package com.delgo.reward.comment.service;

import com.delgo.reward.cert.service.CertCommandService;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.comment.domain.Comment;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.comment.controller.request.CommentCreate;
import com.delgo.reward.comment.controller.request.CommentUpdate;
import com.delgo.reward.comment.controller.request.ReplyCreate;
import com.delgo.reward.comment.repository.CommentRepository;
import com.delgo.reward.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserQueryService userQueryService;
    private final CertCommandService certCommandService;

    @Transactional
    public Comment create(CommentCreate commentCreate) {
        User user = userQueryService.getOneByUserId(commentCreate.userId());
        certCommandService.increaseCommentCount(commentCreate.certificationId());
        return commentRepository.save(Comment.from(commentCreate, user));
    }

    @Transactional
    public Comment create(ReplyCreate replyCreate) {
        User user = userQueryService.getOneByUserId(replyCreate.userId()); // 답글 작성 유저 조회
        certCommandService.increaseCommentCount(replyCreate.certificationId());
        return commentRepository.save(Comment.from(replyCreate, user));
    }

    public Comment getOneById(int commentId){
        return commentRepository.findOneByCommentId(commentId)
                .orElseThrow(() -> new NotFoundDataException("[Comment] commentId : " + commentId));
    }

    public List<Comment> getListByCertId(int certificationId){
        return commentRepository.findListByCertId(certificationId);
    }

    public List<Comment> getListByParentCommentId(int parentCommentId){
        return commentRepository.findListByParentCommentId(parentCommentId);
    }

    @Transactional
    public Comment update(CommentUpdate commentUpdate) {
        Comment comment = getOneById(commentUpdate.commentId());

        comment.setContent(commentUpdate.content());
        commentRepository.save(comment);
        return comment;
    }

    @Transactional
    public void delete(int commentId) {
        commentRepository.deleteById(commentId);
    }
}
