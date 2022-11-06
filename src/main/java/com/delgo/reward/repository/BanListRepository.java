package com.delgo.reward.repository;

import com.delgo.reward.domain.BanList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanListRepository extends JpaRepository<BanList, Integer> {
}
