package com.delgo.reward.repository;


import com.delgo.reward.domain.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Integer>, JpaSpecificationExecutor<Certification> {
    List<Certification> findByUserId(int userId);

    List<Certification> findByUserIdAndCategoryCode(int userId, String categoryCode);

    Optional<Certification> findByCertificationId(int certificationId);

    List<Certification> findByUserIdAndMungpleIdAndRegistDtBetween(int userId, int mungpleId, LocalDateTime start, LocalDateTime end);

    List<Certification> findByUserIdAndCategoryCodeAndRegistDtBetween(int userId, String categoryCode, LocalDateTime start, LocalDateTime end);

    int countByUserIdAndCategoryCode(int userId, String categoryCode);

    int countByUserIdAndCategoryCodeAndMungpleId(int userId, String categoryCode, int mungpleId);

    @Query(value = "select certification_id from certification where user_id not in (select ban_user_id from ban_list where user_id = userId)", nativeQuery = true)
    List<Integer> findByUserIdWithoutBanList(int userId);

    // 가장 최근 등록한 2개의 Certification 조회
    List<Certification> findTop2ByOrderByRegistDtDesc();

}

