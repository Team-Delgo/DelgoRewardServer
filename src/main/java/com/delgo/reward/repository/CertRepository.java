package com.delgo.reward.repository;


import com.delgo.reward.domain.certification.Certification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CertRepository extends JpaRepository<Certification, Integer>, JpaSpecificationExecutor<Certification> {

    Integer countByUserUserId(int userId);
    void deleteAllByUserUserId(int userId);

    @EntityGraph(attributePaths = {"likeLists"})
    @Query("SELECT DISTINCT c FROM Certification c JOIN FETCH c.user u JOIN FETCH u.pet WHERE c.certificationId IN :ids order by c.registDt desc")
    List<Certification> findCertByIds(@Param("ids") List<Integer> ids);

    @EntityGraph(attributePaths = {"likeLists"})
    @Query("SELECT c FROM Certification c JOIN FETCH c.user u JOIN FETCH u.pet WHERE c.certificationId = :certId")
    Optional<Certification> findCertByCertificationId(@Param("certId") Integer certId);

    @Query(value = "select count(c) from Certification c where c.user.userId = :userId and c.mungpleId != 0")
    Integer countOfCertByMungpleAndUser(@Param("userId") int userId);

    @Query(value = "select count(c) from Certification c where c.user.userId = :userId and c.categoryCode = :categoryCode and c.mungpleId = :mungpleId")
    Integer countCertByCategory(@Param("userId") int userId, @Param("categoryCode") String categoryCode, @Param("mungpleId") int mungpleId);

    @Query(value = "select c from Certification c where c.registDt between :startDt and :endDt order by c.registDt desc")
    List<Certification> findCertByDate(@Param("startDt") LocalDateTime startDt, @Param("endDt") LocalDateTime endDate);

    // 자신 거 조회라 is_correct_photo 없어도 됨.
    @EntityGraph(attributePaths = {"user", "likeLists"})
    @Query(value = "select c from Certification c where c.user.userId = :userId and  c.registDt between :startDt and :endDt order by c.registDt desc")
    List<Certification> findCertByDateAndUser(@Param("userId") int userId, @Param("startDt") LocalDateTime startDt, @Param("endDt") LocalDateTime endDate);

    @Query(value = "select c.certificationId from Certification c where c.user.userId  not in (select b.banUserId from BanList b where b.userId = :userId) and c.isCorrectPhoto = true")
    Slice<Integer> findAllCertIdByPaging(@Param("userId") int userId, Pageable pageable);

    @Query(value = "select c.certificationId from Certification c where c.user.userId not in (select b.banUserId from BanList b where b.userId = :userId) and c.certificationId != :certificationId and c.isCorrectPhoto = true")
    Slice<Integer> findAllExcludeSpecificCert(@Param("userId") int userId, @Param("certificationId") int certificationId, Pageable pageable);

    // 자기 자신 인증 조회
    @Query(value = "select c.certificationId from Certification c where c.user.userId = :userId")
    List<Integer> findCertIdByUserUserId(@Param("userId") int userId);

    @Query(value = "SELECT c.certificationId FROM Certification c where c.isExpose = true and c.isCorrectPhoto = true order by RAND()")
    List<Integer> findCertIdByIsExpose(Pageable pageable);

    @Query(value = "select c.certificationId from Certification c where c.user.userId = :userId")
    Slice<Integer> findByUserUserId(@Param("userId") int userId, Pageable pageable);

    @Query(value = "select c.certificationId from Certification c where c.user.userId = :userId and c.categoryCode = :categoryCode")
    Slice<Integer> findByUserUserIdAndCategoryCode(@Param("userId")int userId, @Param("categoryCode") String categoryCode, Pageable pageable);

    @Query(value = "select c.certificationId from Certification c where c.mungpleId = :mungpleId and c.isCorrectPhoto = true")
    Slice<Integer> findCertByMungple(@Param("mungpleId") int mungpleId, Pageable pageable);

    @Query(value = "select c.certificationId from Certification c where c.user.userId not in (select b.banUserId from BanList b where b.userId = :userId) and c.isCorrectPhoto = true order by c.registDt desc")
    List<Integer> findRecentCert(@Param("userId") int userId, Pageable pageable);

    @Query("SELECT c.mungpleId FROM Certification c where c.mungpleId != 0 GROUP BY c.mungpleId ORDER BY COUNT(c) DESC")
    List<Integer>  findCertOrderByMungpleCount(Pageable pageable);



    // ---------------------------------------- Map TEST ----------------------------------------

    @Query(value = "SELECT c FROM Certification c where c.geoCode = :geoCode order by RAND()")
    List<Certification> findByGeoCode(@Param("geoCode") String geoCode, Pageable pageable);

    @Query(value = "SELECT c FROM Certification c where c.pGeoCode = :pGeoCode order by RAND()")
    List<Certification> findByPGeoCode(@Param("pGeoCode") String pGeoCode, Pageable pageable);

    @Query(value = "SELECT c FROM Certification c where c.pGeoCode = :pGeoCode and not c.geoCode = :geoCode order by RAND()")
    List<Certification> findByPGeoCodeExceptGeoCode(@Param("pGeoCode") String pGeoCode, @Param("geoCode") String geoCode, Pageable pageable);
}