package com.delgo.reward.repository;


import com.delgo.reward.domain.Certification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CertRepository extends JpaRepository<Certification, Integer>, JpaSpecificationExecutor<Certification> {
    List<Certification> findByUserId(int userId);

    // 페이징
    Slice<Certification> findByUserId(int userId, Pageable pageable);

    @Query(value = "select * from certification where user_id  not in (select ban_user_id from ban_list where user_id = ?)", nativeQuery = true)
    Slice<Certification> findAllByPaging(int userId, Pageable pageable);

    // Live 인증만 조회
    List<Certification> findByUserIdAndIsLive(int userId, boolean isLive);

    Optional<Certification> findByCertificationId(int certificationId);

    // ----- Cert 가능 여부 Check -----
    List<Certification> findByUserIdAndMungpleIdAndIsLiveAndRegistDtBetween(int userId, int mungpleId, Boolean isLive, LocalDateTime start, LocalDateTime end);
    List<Certification> findByUserIdAndCategoryCodeAndIsLiveAndRegistDtBetween(int userId, String categoryCode, boolean isLive, LocalDateTime start, LocalDateTime end);

    int countByUserIdAndCategoryCode(int userId, String categoryCode);

    int countByUserIdAndCategoryCodeAndMungpleId(int userId, String categoryCode, int mungpleId);

    @Query(value = "select certification_id from certification where user_id not in (select ban_user_id from ban_list where user_id = userId)", nativeQuery = true)
    List<Integer> findByUserIdWithoutBanList(int userId);

    @Query(value = "select * from certification where user_id  not in (select ban_user_id from ban_list where user_id = ?) order by regist_dt desc limit 2", nativeQuery = true)
    List<Certification> findTop2ByOrderByRegistDtDesc(int userId);

    Slice<Certification> findByUserIdAndCategoryCode(int userId, String categoryCode, Pageable pageable);
}