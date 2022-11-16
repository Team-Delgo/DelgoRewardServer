package com.delgo.reward.repository;

import com.delgo.reward.domain.LikeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LikeListRepository extends JpaRepository<LikeList, Integer>, JpaSpecificationExecutor<LikeList> {
    Optional<LikeList> findByUserIdAndCertificationId(int userId, int certificationId);

    Integer deleteByUserIdAndCertificationId(int userId, int certificationId);
}
