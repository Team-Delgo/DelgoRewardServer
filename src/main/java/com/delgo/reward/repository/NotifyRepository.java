package com.delgo.reward.repository;

import com.delgo.reward.domain.notify.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, Integer> {
    List<Notify> findAllByUserIdOrderByCreateAtDesc(int userId);
}
