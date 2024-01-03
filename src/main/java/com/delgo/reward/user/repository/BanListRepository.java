package com.delgo.reward.user.repository;

import com.delgo.reward.user.domain.BanList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanListRepository extends JpaRepository<BanList, Integer> {
}
