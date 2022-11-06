package com.delgo.reward.repository;


import com.delgo.reward.domain.WardOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface WardOfficeRepository extends JpaRepository<WardOffice, Integer>, JpaSpecificationExecutor<WardOffice> {

    Optional<WardOffice> findByGeoCode(String geoCode);
}