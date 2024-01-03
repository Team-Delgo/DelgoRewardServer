package com.delgo.reward.notify.repository;

import com.delgo.reward.notify.domain.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, Integer> {
    List<Notify> findAllByUserIdOrderByCreateAtDesc(int userId);
}
