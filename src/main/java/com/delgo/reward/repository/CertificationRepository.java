package com.delgo.reward.repository;


import com.delgo.reward.domain.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Integer>, JpaSpecificationExecutor<Certification> {
    List<Certification> findByUserId(int userId);
}
