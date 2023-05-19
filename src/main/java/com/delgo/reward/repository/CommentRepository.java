package com.delgo.reward.repository;

import com.delgo.reward.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
//    @Query(value = "select c.*, u.name, u.profile from comment c join user u on c.user_id = u.user_id where certification_id = ? and is_reply = false", nativeQuery = true)
//    List<Comment> findByCertificationId(int certificationId);

    void deleteAllByUserId(int userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update comment set content = :updateContent where comment_id = :commentId", nativeQuery = true)
    void updateByCommentId(@Param(value="commentId") int commentId, @Param(value="updateContent") String content);

    @Query(value = "select * from comment where parent_comment_id = ? and is_reply = true", nativeQuery = true)
    List<Comment> findByParentCommentId(int parentCommentId);

    @Query(value = "select count(*) from Comment c where c.certificationId = :certId")
    Integer countCommentByCertId(@Param("certId") int certId);
}
