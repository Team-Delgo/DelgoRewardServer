package com.delgo.reward.repository;


import com.delgo.reward.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Integer> {
    Optional<Point> findByUserId(int userId);

    @Query(value = "update point set last_weekly_point = 0", nativeQuery = true)
    void initLastWeeklyPoint();

    @Query(value = "update point set last_weekly_point = weekly_point", nativeQuery = true)
    void setLastWeeklyPoint();

    @Query(value = "update point set weekly_point = 0", nativeQuery = true)
    void initWeeklyPoint();
}
