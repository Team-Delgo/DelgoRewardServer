package com.delgo.reward.repository;


import com.delgo.reward.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Integer> {
    Optional<Point> findByUserId(int userId);
}
