package com.delgo.reward.repository;


import com.delgo.reward.domain.certification.Certification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CertRepository extends JpaRepository<Certification, Integer>, JpaSpecificationExecutor<Certification> {
    List<Certification> findByUserId(int userId);
    void deleteAllByUserId(int userId);

    // 페이징
    Slice<Certification> findByUserId(int userId, Pageable pageable);
    Slice<Certification> findByUserIdAndCategoryCode(int userId, String categoryCode, Pageable pageable);

    // Live 인증만 조회
    List<Certification> findByUserIdAndIsLive(int userId, boolean isLive);

    // ----- Cert 가능 여부 Check -----
    List<Certification> findByUserIdAndCategoryCodeAndIsLiveAndRegistDtBetween(int userId, String categoryCode, boolean isLive, LocalDateTime start, LocalDateTime end);

    @Query(value = "select * from certification where user_id  not in (select ban_user_id from ban_list where user_id = ?)", nativeQuery = true)
    Slice<Certification> findAllByPaging(int userId, Pageable pageable);

    @Query(value = "select * from certification where user_id  not in (select ban_user_id from ban_list where user_id = ?) order by regist_dt desc limit ?", nativeQuery = true)
    List<Certification> findRecentCert(int userId, int count);

    @Query(value = "select count(category_code) from certification where user_id = ? and category_code = ? and mungple_id = ?", nativeQuery = true)
    Integer countByCategory(int userId, String categoryCode, int mungpleId);

    Integer countByUserId(int userId);
}