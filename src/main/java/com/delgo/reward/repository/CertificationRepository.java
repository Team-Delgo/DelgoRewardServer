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

public interface CertificationRepository extends JpaRepository<Certification, Integer>, JpaSpecificationExecutor<Certification> {
    List<Certification> findByUserId(int userId);

    // 페이징
    Slice<Certification> findByUserId(int userId, Pageable pageable);

    @Query("SELECT u as user, c as cert FROM Certification c LEFT JOIN User u ON u.userId = c.userId")
    Slice<Certification> findAllByPaging(Pageable pageable);

    // Live 인증만 조회
    List<Certification> findByUserIdAndIsLive(int userId, int isLive);

    Optional<Certification> findByCertificationId(int certificationId);

    List<Certification> findByUserIdAndMungpleIdAndRegistDtBetween(int userId, int mungpleId, LocalDateTime start, LocalDateTime end);

    List<Certification> findByUserIdAndCategoryCodeAndRegistDtBetween(int userId, String categoryCode, LocalDateTime start, LocalDateTime end);

    int countByUserIdAndCategoryCode(int userId, String categoryCode);

    int countByUserIdAndCategoryCodeAndMungpleId(int userId, String categoryCode, int mungpleId);

    @Query(value = "select certification_id from certification where user_id not in (select ban_user_id from ban_list where user_id = userId)", nativeQuery = true)
    List<Integer> findByUserIdWithoutBanList(int userId);

    @Query(value = "select * from certification where user_id  not in (select ban_user_id from ban_list where user_id = ?) limit 2", nativeQuery = true)
    List<Certification> findTop2ByOrderByRegistDtDesc(int userId);

    @Query(value = "select * from certification where user_id  not in (select ban_user_id from ban_list where user_id = ?)", nativeQuery = true)
    Slice<Certification> findByUserIdAndCategoryCode(int userId, String categoryCode, Pageable pageable);
}

