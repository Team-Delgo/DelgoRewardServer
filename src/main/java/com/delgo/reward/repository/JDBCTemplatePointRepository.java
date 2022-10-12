package com.delgo.reward.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JDBCTemplatePointRepository {
    private final JdbcTemplate jdbcTemplate;

    public void updateAccumulatedPoint(int userId, int categoryPoint){
        jdbcTemplate.update("update point set accumulated_point = accumulated_point + ? where user_id = ?", categoryPoint, userId);
    }

    public void updateWeeklyPoint(int userId, int categoryPoint){
        jdbcTemplate.update("update point set weekly_point = weekly_point + ? where user_id = ?", categoryPoint, userId);
    }
}
