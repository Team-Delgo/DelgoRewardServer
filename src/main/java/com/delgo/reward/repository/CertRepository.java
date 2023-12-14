package com.delgo.reward.repository;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CertRepository extends JpaRepository<Certification, Integer>, JpaSpecificationExecutor<Certification> {

    Integer countByUserUserIdAndIsCorrect(int userId, boolean isCorrect);
    void deleteAllByUserUserId(int userId);

//    @EntityGraph(attributePaths = {"photos"})
    @Query("SELECT DISTINCT c FROM Certification c JOIN FETCH c.user u JOIN FETCH u.pet WHERE c.certificationId IN :ids order by c.registDt desc")
    List<Certification> findCertByIds(@Param("ids") List<Integer> ids);

//    @EntityGraph(attributePaths = {"photos"})
    @Query("SELECT c FROM Certification c JOIN FETCH c.user u JOIN FETCH u.pet WHERE c.certificationId = :certId")
    Optional<Certification> findCertByCertificationId(@Param("certId") Integer certId);


    @Query(value = "select count(c) from Certification c where c.mungpleId = :mungpleId and c.isCorrect = true")
    Integer countOfCorrectCertByMungple(@Param("mungpleId") int mungpleId);


    @Query(value = "select c from Certification c where c.registDt between :startDt and :endDt order by c.registDt desc")
    List<Certification> findCertByDate(@Param("startDt") LocalDateTime startDt, @Param("endDt") LocalDateTime endDate);

    // 자신 거 조회라 is_correct_photo 없어도 됨.
    @EntityGraph(attributePaths = {"user"})
    @Query(value = "select c from Certification c where c.user.userId = :userId and  c.registDt between :startDt and :endDt order by c.registDt desc")
    List<Certification> findCertByDateAndUser(@Param("userId") int userId, @Param("startDt") LocalDateTime startDt, @Param("endDt") LocalDateTime endDate);

    @Query(value = "select c.certificationId from Certification c where c.user.userId = :userId")
    List<Integer> findCertIdByUserUserId(@Param("userId") int userId);


    @Query(value = "select c.certificationId from Certification c where c.user.userId = :userId")
    List<Integer> findAllCertIdByUserId(@Param("userId") int userId);


    // ---------------------- isCorrect -----------------------------


    @Query(value = "select c.certificationId from Certification c where c.user.userId not in (select b.banUserId from BanList b where b.userId = :userId) and c.isCorrect = true order by c.registDt desc")
    List<Integer> findRecentCertId(@Param("userId") int userId, Pageable pageable);

    @Query(value = "select new com.delgo.reward.dto.user.UserVisitMungpleCountDTO(c.mungpleId, COUNT(c.mungpleId)) from Certification c where c.user.userId = :userId and c.mungpleId > 0 group by c.mungpleId having count(c.mungpleId) > 0 order by count(c.mungpleId) desc")
    List<UserVisitMungpleCountDTO> findVisitTop3MungpleIdByUserId(@Param("userId") int userId, Pageable pageable);

    @Query(value = "select new com.delgo.reward.dto.mungple.MungpleCountDTO(c.mungpleId, count(c)) from Certification c where c.isCorrect = true and c.mungpleId != 0 group by c.mungpleId order by count(c) desc")
    List<MungpleCountDTO> countCertsGroupedByMungpleId();
  
    // ---------------------------------------- Map TEST ----------------------------------------



    @EntityGraph(attributePaths = {"user", "user.pet"})
    @Query(value = "select c from Certification c where c.user.userId not in (select b.banUserId from BanList b where b.userId = :userId) and c.isCorrect = true")
    Page<Certification> findCorrectPage(@Param("userId") int userId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "user.pet"})
    @Query(value = "select c from Certification c where c.mungpleId = :mungpleId and c.isCorrect = true and c.user.userId not in (select b.banUserId from BanList b where b.userId = :userId)")
    Page<Certification> findCorrectPageByMungple(@Param("mungpleId") int mungpleId, @Param("userId") int userId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "user.pet"})
    @Query(value = "select c from Certification c where c.user.userId = :userId")
    Page<Certification> findPageByUserId(@Param("userId") int userId, Pageable pageable);

    @Query(value = "select c from Certification c where c.user.userId = :userId and c.isCorrect = true")
    List<Certification> findCorrectCertByUserId(@Param("userId") int userId);

    @EntityGraph(attributePaths = {"user", "user.pet"})
    @Query(value = "select c from Certification c where c.user.userId = :userId and c.categoryCode = :categoryCode")
    Page<Certification> findPageByUserIdAndCategoryCode(@Param("userId")int userId, @Param("categoryCode") CategoryCode categoryCode, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "user.pet"})
    @Query(value = "select c from Certification c where c.user.userId = :userId and c.isCorrect = true and c.user.userId not in (select b.banUserId from BanList b where b.userId = :userId)")
    Page<Certification> findCorrectPageByUserId(@Param("userId") int userId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "user.pet"})
    @Query(value = "select c from Certification c where c.user.userId = :userId and c.categoryCode = :categoryCode and c.isCorrect = true and c.user.userId not in (select b.banUserId from BanList b where b.userId = :userId)")
    Page<Certification> findCorrectPageByUserIdAndCategoryCode(@Param("userId")int userId, @Param("categoryCode") CategoryCode categoryCode, Pageable pageable);
}