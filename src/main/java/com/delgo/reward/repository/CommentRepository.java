package com.delgo.reward.repository;

import com.delgo.reward.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
//    @Query(value = "select c.*, u.name, u.profile from comment c join user u on c.user_id = u.user_id where certification_id = ? and is_reply = false", nativeQuery = true)
//    List<Comment> findByCertificationId(int certificationId);

    @Query(value = "select * from comment where parent_comment_id = ? and is_reply = true", nativeQuery = true)
    List<Comment> findByParentCommentId(int parentCommentId);
}
