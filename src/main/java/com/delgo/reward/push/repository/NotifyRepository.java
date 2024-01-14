package com.delgo.reward.push.repository;

import com.delgo.reward.push.domain.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, Integer> {
    List<Notify> findListByUserIdOrderByCreateAtDesc(int userId);
}
