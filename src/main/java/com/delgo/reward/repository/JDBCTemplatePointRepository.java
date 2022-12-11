package com.delgo.reward.repository;

import com.delgo.reward.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JDBCTemplatePointRepository {
    private final JdbcTemplate jdbcTemplate;

    public void createUserPoint(User user){
        jdbcTemplate.update("insert into point (user_id, geo_code, p_geo_code) values (?, ?, ?)", user.getUserId(), user.getGeoCode(), user.getPGeoCode());
    }

    public void deleteAllByUserId(int userId){
        jdbcTemplate.update("delete from point where user_id = " + userId);
    }

    public void updateAccumulatedPoint(int userId, int categoryPoint){
        jdbcTemplate.update("update point set accumulated_point = accumulated_point + ? where user_id = ?", categoryPoint, userId);
    }

    public void updateWeeklyPoint(int userId, int categoryPoint){
        jdbcTemplate.update("update point set weekly_point = weekly_point + ? where user_id = ?", categoryPoint, userId);
    }

}
