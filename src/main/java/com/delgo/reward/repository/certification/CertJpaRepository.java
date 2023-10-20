package com.delgo.reward.repository.certification;


import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CertJpaRepository extends JpaRepository<Certification, Integer>, CertQueryRepository {
    void deleteByUserUserId(int userId);

    @Query(value = "select count(c) from Certification c where c.mungpleId = :mungpleId and c.isCorrect = true")
    Integer countOfCorrectByMungpleId(@Param("mungpleId") int mungpleId);

    @Query("select c from Certification c join fetch c.user u join fetch u.pet where c.certificationId = :certId")
    Optional<Certification> findByCertId(@Param("certId") Integer certId);

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