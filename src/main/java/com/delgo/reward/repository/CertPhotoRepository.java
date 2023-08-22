package com.delgo.reward.repository;


import com.delgo.reward.domain.certification.CertPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface CertPhotoRepository extends JpaRepository<CertPhoto, Integer>, JpaSpecificationExecutor<CertPhoto> {
    List<CertPhoto> findPhotosByCertificationId(int certId);
}