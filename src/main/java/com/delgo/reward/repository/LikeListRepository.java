package com.delgo.reward.repository;

import com.delgo.reward.domain.like.LikeList;
import com.delgo.reward.domain.like.LikeListPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LikeListRepository extends JpaRepository<LikeList, LikeListPK>, JpaSpecificationExecutor<LikeList> {
    Optional<LikeList> findByUserIdAndCertificationId(int userId, int certificationId);
}
