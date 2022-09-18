package com.delgo.reward.repository;

import com.delgo.reward.dto.RankingByPointDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JDBCTemplateRankingRepository{

    private final JdbcTemplate jdbcTemplate;

    public List<RankingByPointDTO> findRankingByPoint() {
        return jdbcTemplate.query("select user_id, point, RANK() over (order by point desc) ranking from user", rankingRowMapper());
    }

    private RowMapper<RankingByPointDTO> rankingRowMapper() {
        return (rs, rowNum) -> {
            RankingByPointDTO rankingByPointDTO = RankingByPointDTO.builder().userId(rs.getInt("user_id")).ranking(rs.getInt("ranking")).build();
            return rankingByPointDTO;
        };
    }
}
