package com.delgo.reward.repository;

import com.delgo.reward.domain.like.LikeList;
import com.delgo.reward.domain.like.LikeListPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface LikeListRepository extends JpaRepository<LikeList, LikeListPK>, JpaSpecificationExecutor<LikeList> {
    Optional<LikeList> findByUserIdAndCertificationId(int userId, int certificationId);
    void deleteByCertificationId(int certificateId);
    void deleteByUserId(int userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE like_list SET is_like = NOT is_like WHERE user_id = ? AND certification_id = ?", nativeQuery = true)
    void updateIsLike(Integer userId, Integer certificationId);

    @Query(value = "SELECT COUNT(*) FROM like_list WHERE certification_id = ? AND is_like = true", nativeQuery = true)
    int countByCertificationIdAndIsLike(int certificationId);
}
