package com.delgo.reward.repository;

import com.delgo.reward.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
    @Query(value = "select c from Comment c join fetch c.user u join fetch u.pet where c.certificationId = :certId")
    List<Comment> findCommentsByCertId(@Param("certId") int certId);

    @Query(value = "select c from Comment c join fetch c.user u join fetch u.pet where c.commentId = :commentId")
    Optional<Comment> findCommentsById(@Param("commentId") int commentId);

    @Query(value = "select count(*) from Comment c where c.certificationId = :certId")
    Integer countCommentByCertId(@Param("certId") int certId);

    @Query(value = "select c from Comment c where c.parentCommentId = :parentCommentId and c.isReply = true")
    List<Comment> findCommentsByParentCommentId(@Param("parentCommentId") int parentCommentId);
}
