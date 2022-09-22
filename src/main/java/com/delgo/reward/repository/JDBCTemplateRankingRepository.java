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


    public void insertRanking(Ranking ranking){
        String sql = "insert into ranking values(?,?,?,?)";
        jdbcTemplate.update(sql, ranking.getGeoCode(), ranking.getCategoryCode(), ranking.getRanking(), ranking.getUserId());
    }

    public List<Ranking> findRankingByPoint() {
        return jdbcTemplate.query("select user_id, geo_code, weekly_point, RANK() over (partition by geo_code order by weekly_point desc) ranking from user;", rankingByPointRowMapper());
    }

    public List<Ranking> findRankingByCategory(String categoryCode) {
        return jdbcTemplate.query("select category_code, user_id, geo_code, RANK() over (partition by category_code order by user_id desc) ranking from (select category_code, user_id, geo_code, count(*) from certification where category_code = \"" + categoryCode + "\" group by user_id) by_category_code;", rankingByCategoryRowMapper(categoryCode));
    }

    public List<Ranking> selectRanking(int userId, String categoryCode){
        return jdbcTemplate.query("select * from ranking where user_id = " + userId + " and category_code = \"" + categoryCode + "\";", userByCategoryRowMapper());
    }

    private RowMapper<Ranking> userByCategoryRowMapper(){
        return (rs, rowNum) -> {
            Ranking ranking = Ranking.builder().userId(rs.getInt("user_id")).ranking(rs.getInt("ranking")).geoCode(rs.getString("geo_code")).categoryCode("CA0000").build();
            return ranking;
        };
    }

    private RowMapper<Ranking> rankingByPointRowMapper() {
        return (rs, rowNum) -> {
            Ranking ranking = Ranking.builder().userId(rs.getInt("user_id")).ranking(rs.getInt("ranking")).geoCode(rs.getString("geo_code")).categoryCode("CA0000").build();
            return ranking;
        };
    }

    private RowMapper<Ranking> rankingByCategoryRowMapper(String categoryCode) {
        return (rs, rowNum) -> {
            Ranking ranking = Ranking.builder().userId(rs.getInt("user_id")).ranking(rs.getInt("ranking")).geoCode(rs.getString("geo_code")).categoryCode(categoryCode).build();
            return ranking;
        };
    }
}
