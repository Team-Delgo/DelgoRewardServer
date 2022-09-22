package com.delgo.reward.repository;

import com.delgo.reward.domain.Ranking;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JDBCTemplateRankingRepository{

    private final JdbcTemplate jdbcTemplate;

    public List<Ranking> findRankingByPoint() {
        return jdbcTemplate.query("select user_id, geo_code, weekly_point, RANK() over (partition by geo_code order by weekly_point desc) ranking from user;", rankingRowMapper());
    }

    private RowMapper<Ranking> rankingRowMapper() {
        return (rs, rowNum) -> {
            Ranking ranking = Ranking.builder().userId(rs.getInt("user_id")).ranking(rs.getInt("ranking")).geoCode(rs.getString("geo_code")).categoryCode("CA0000").build();
            return ranking;
        };
    }
}
