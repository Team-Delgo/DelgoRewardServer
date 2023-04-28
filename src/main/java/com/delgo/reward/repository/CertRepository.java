package com.delgo.reward.repository;


import com.delgo.reward.domain.certification.Certification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CertRepository extends JpaRepository<Certification, Integer>, JpaSpecificationExecutor<Certification> {

    Integer countByUserId(int userId);
    void deleteAllByUserId(int userId);
    List<Certification> findByUserId(int userId);
    Slice<Certification> findByUserId(int userId, Pageable pageable);
    Slice<Certification> findByUserIdAndCategoryCode(int userId, String categoryCode, Pageable pageable);

    // 자신 거 조회라 is_correct_photo 없어도 됨.
    @Query(value = "select c from Certification c where c.userId = :userId and  c.registDt between :startDt and :endDt order by c.registDt desc")
    List<Certification> findCertByDateAndUser(@Param("userId") int userId, @Param("startDt") LocalDate startDt, @Param("endDt") LocalDate endDate);

    @Query(value = "select c from Certification c where c.registDt between :startDt and :endDt order by c.registDt desc")
    List<Certification> findCertByDate(@Param("startDt") LocalDate startDt, @Param("endDt") LocalDate endDate);

    // 전체 조회
    @Query(value = "select c from Certification c where c.userId  not in (select b.banUserId from BanList b where b.userId = :userId) and c.isCorrectPhoto = true")
    Slice<Certification> findAllByPaging(@Param("userId") int userId, Pageable pageable);

    @Query(value = "select c from Certification c where c.mungpleId = :mungpleId and c.isCorrectPhoto = true")
    Slice<Certification> findCertByMungple(@Param("mungpleId") int mungpleId, Pageable pageable);

    @Query(value = "select count(c) from Certification c where c.userId = :userId and c.mungpleId != 0")
    Integer countOfCertByMungpleAndUser(@Param("userId") int userId);

    @Query(value = "select c from Certification c where c.userId not in (select b.banUserId from BanList b where b.userId = :userId) and c.isCorrectPhoto = true order by c.registDt desc")
    List<Certification> findRecentCert(@Param("userId") int userId, Pageable pageable);

    @Query(value = "SELECT c FROM Certification c where c.isExpose = true and c.isCorrectPhoto = true order by RAND()")
    List<Certification> findByIsExpose(Pageable pageable);

    @Query(value = "SELECT c FROM Certification c where c.pGeoCode = :pGeoCode order by RAND()")
    List<Certification> findByPGeoCode(@Param("pGeoCode") String pGeoCode, Pageable pageable);

    @Query(value = "SELECT c FROM Certification c where c.geoCode = :geoCode order by RAND()")
    List<Certification> findByGeoCode(@Param("geoCode") String geoCode, Pageable pageable);

    @Query(value = "SELECT c FROM Certification c where c.pGeoCode = :pGeoCode and not c.geoCode = :geoCode order by RAND()")
    List<Certification> findByPGeoCodeExceptGeoCode(@Param("pGeoCode") String pGeoCode, @Param("geoCode") String geoCode, Pageable pageable);

    @Query(value = "select c from Certification c where c.userId not in (select b.banUserId from BanList b where b.userId = :userId) and c.certificationId != :certificationId and c.isCorrectPhoto = true")
    Slice<Certification> findAllExcludeSpecificCert(@Param("userId") int userId, @Param("certificationId") int certificationId, Pageable pageable);

    @Query(value = "select count(c) from Certification c where c.userId = :userId and c.categoryCode = :categoryCode and c.mungpleId = :mungpleId")
    Integer countCertByCategory(@Param("userId") int userId, @Param("categoryCode") String categoryCode, @Param("mungpleId") int mungpleId);
}