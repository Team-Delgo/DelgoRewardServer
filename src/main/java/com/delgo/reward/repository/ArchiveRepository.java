package com.delgo.reward.repository;


import com.delgo.reward.domain.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ArchiveRepository extends JpaRepository<Archive, Integer>, JpaSpecificationExecutor<Archive> {
    List<Archive> findByUserId(int userId);
}