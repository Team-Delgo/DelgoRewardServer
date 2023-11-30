package com.delgo.reward.certification.infrastructure.jpa;


import com.delgo.reward.certification.infrastructure.query.CertQueryRepository;
import com.delgo.reward.certification.infrastructure.entity.CertificationEntity;
import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.dto.user.VisitCountDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CertJpaRepository extends JpaRepository<CertificationEntity, Integer>, CertQueryRepository {
    void deleteByUserUserId(int userId);

    @Query("select c from CertificationEntity c join fetch c.user u join fetch u.pet where c.certificationId = :certId")
    Optional<CertificationEntity> findByCertId(@Param("certId") Integer certId);

    @Query(value = "select count(c) from CertificationEntity c where c.mungpleId = :mungpleId and c.isCorrect = true")
    Integer countOfCorrectByMungpleId(@Param("mungpleId") int mungpleId);

    // ---------------------------------------- USE DTO ----------------------------------------

    @Query(value = "select new com.delgo.reward.dto.user.VisitCountDTO(c.mungpleId, COUNT(c.mungpleId)) from CertificationEntity c where c.user.userId = :userId and c.mungpleId > 0 group by c.mungpleId having count(c.mungpleId) > 0 order by count(c.mungpleId) desc")
    List<VisitCountDTO> findVisitTop3MungpleIdByUserId(@Param("userId") int userId, Pageable pageable);

    @Query(value = "select new com.delgo.reward.dto.mungple.MungpleCountDTO(c.mungpleId, count(c)) from CertificationEntity c where c.isCorrect = true and c.mungpleId != 0 group by c.mungpleId order by count(c) desc")
    List<MungpleCountDTO> countGroupedByMungpleId();
}