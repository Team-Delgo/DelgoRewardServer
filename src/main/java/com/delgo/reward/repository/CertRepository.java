package com.delgo.reward.repository;


import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
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
    void deleteAllByUserUserId(int userId);

    @Query(value = "select count(c) from Certification c where c.user.userId = :userId and c.isCorrect = true")
    Integer countOfCorrectByUserId(@Param("userId") int userId);

    @Query(value = "select count(c) from Certification c where c.mungpleId = :mungpleId and c.isCorrect = true")
    Integer countOfCorrectByMungpleId(@Param("mungpleId") int mungpleId);

    @EntityGraph(attributePaths = {"photos"})
    @Query("select c from Certification c join fetch c.user u join fetch u.pet where c.certificationId = :certId")
    Optional<Certification> findByCertId(@Param("certId") Integer certId);

    @EntityGraph(attributePaths = {"photos"})
    @Query("select distinct c from Certification c join fetch c.user u join fetch u.pet where c.certificationId in :ids order by c.registDt desc")
    List<Certification> findListByIds(@Param("ids") List<Integer> ids);

    @Query(value = "select c from Certification c where c.registDt between :startDt and :endDt order by c.registDt desc")
    List<Certification> findListByDate(@Param("startDt") LocalDateTime startDt, @Param("endDt") LocalDateTime endDate);

    @EntityGraph(attributePaths = {"photos"})
    @Query(value = "select c from Certification c where c.user.userId = :userId and  c.registDt between :startDt and :endDt order by c.registDt desc")
    List<Certification> findListByDateAndUserId(@Param("userId") int userId, @Param("startDt") LocalDateTime startDt, @Param("endDt") LocalDateTime endDate);

    @Query(value = "select c.certificationId from Certification c where c.user.userId = :userId")
    Slice<Integer> findIdsByUserId(@Param("userId") int userId, Pageable pageable);

    @Query(value = "select c.certificationId from Certification c where c.isCorrect = true")
    Slice<Integer> findCorrectIds(Pageable pageable);

    @Query(value = "select c.certificationId from Certification c where c.mungpleId = :mungpleId and c.isCorrect = true")
    Slice<Integer> findCorrectIdsByMungpleId(@Param("mungpleId") int mungpleId, Pageable pageable);

    @Query(value = "select c.certificationId from Certification c where c.user.userId = :userId and c.isCorrect = true")
    Slice<Integer> findCorrectIdsByUserId(@Param("userId") int userId, Pageable pageable);

    // ---------------------------------------- USE DTO ----------------------------------------

    @Query(value = "select new com.delgo.reward.dto.user.UserVisitMungpleCountDTO(c.mungpleId, COUNT(c.mungpleId)) from Certification c where c.user.userId = :userId and c.mungpleId > 0 group by c.mungpleId having count(c.mungpleId) > 0 order by count(c.mungpleId) desc")
    List<UserVisitMungpleCountDTO> findVisitTop3MungpleIdByUserId(@Param("userId") int userId, Pageable pageable);

    @Query(value = "select new com.delgo.reward.dto.mungple.MungpleCountDTO(c.mungpleId, count(c)) from Certification c where c.isCorrect = true and c.mungpleId != 0 group by c.mungpleId order by count(c) desc")
    List<MungpleCountDTO> countGroupedByMungpleId();
  
    // ---------------------------------------- Map TEST ----------------------------------------

    @Query(value = "SELECT c FROM Certification c where c.geoCode = :geoCode order by RAND()")
    List<Certification> findCertByGeoCode(@Param("geoCode") String geoCode, Pageable pageable);

    @Query(value = "SELECT c FROM Certification c where c.pGeoCode = :pGeoCode order by RAND()")
    List<Certification> findCertByPGeoCode(@Param("pGeoCode") String pGeoCode, Pageable pageable);

    @Query(value = "SELECT c FROM Certification c where c.pGeoCode = :pGeoCode and not c.geoCode = :geoCode order by RAND()")
    List<Certification> findCertByPGeoCodeExceptGeoCode(@Param("pGeoCode") String pGeoCode, @Param("geoCode") String geoCode, Pageable pageable);
}