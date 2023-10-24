package com.delgo.reward.certification.infrastructure.jpa;


import com.delgo.reward.certification.infrastructure.entity.CertPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface CertPhotoJpaRepository extends JpaRepository<CertPhotoEntity, Integer>, JpaSpecificationExecutor<CertPhotoEntity> {
    List<CertPhotoEntity> findByCertificationId(int certificationId);
    List<CertPhotoEntity> findByCertificationIdIn(List<Integer> certificationIdList);
}