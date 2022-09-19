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
        return jdbcTemplate.query("select user_id, address, weekly_point, RANK() over (order by weekly_point desc) ranking from user group by address", rankingRowMapper());
    }

    private RowMapper<Ranking> rankingRowMapper() {
        return (rs, rowNum) -> {
            Ranking ranking = Ranking.builder().userId(rs.getInt("user_id")).ranking(rs.getInt("ranking")).geoCode(rs.getString("address")).categoryCode("CA0000").build();
            return ranking;
        };
    }
}
