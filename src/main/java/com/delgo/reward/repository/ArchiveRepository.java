package com.delgo.reward.repository;


import com.delgo.reward.domain.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ArchiveRepository extends JpaRepository<Archive, Integer>, JpaSpecificationExecutor<Archive> {
    List<Archive> findByUserId(int userId);

    List<Archive> findByUserIdAndIsMainNot(int userId, int isMain);

    Optional<Archive> findByUserIdAndAchievementsId(int userId, int achievementsId);

}