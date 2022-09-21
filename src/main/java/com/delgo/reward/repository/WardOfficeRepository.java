package com.delgo.reward.repository;


import com.delgo.reward.domain.WardOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WardOfficeRepository extends JpaRepository<WardOffice, Integer>, JpaSpecificationExecutor<WardOffice> {
}