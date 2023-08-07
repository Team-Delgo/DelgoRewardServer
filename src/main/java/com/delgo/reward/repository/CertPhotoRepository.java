package com.delgo.reward.repository;


import com.delgo.reward.domain.certification.CertPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CertPhotoRepository extends JpaRepository<CertPhoto, Integer>, JpaSpecificationExecutor<CertPhoto> {
}