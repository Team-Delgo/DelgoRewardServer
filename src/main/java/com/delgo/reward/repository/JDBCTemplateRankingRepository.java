package com.delgo.reward.repository;

import com.delgo.reward.domain.ranking.RankingCategory;
import com.delgo.reward.domain.ranking.RankingPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JDBCTemplateRankingRepository{

    private final JdbcTemplate jdbcTemplate;

    public List<RankingPoint> findTopPointRankingByGeoCode(String geoCode){
        return jdbcTemplate.query("select user_id, ranking, weekly_point from ranking_point where geo_code = " + geoCode + " limit 3;", pointRankingByGeoCodeRowMapper(geoCode));
    }

    private RowMapper<RankingPoint> pointRankingByGeoCodeRowMapper(String geoCode) {
        return (rs, rowNum) -> {
            RankingPoint rankingPoint = RankingPoint.builder().userId(rs.getInt("user_id")).ranking(rs.getInt("ranking")).weeklyPoint(rs.getInt("weekly_point")).geoCode(geoCode).build();
            return rankingPoint;
        };
    }

    public List<RankingPoint> findRankingByPoint() {
        return jdbcTemplate.query("select user_id, geo_code, weekly_point, RANK() over (partition by geo_code order by weekly_point desc) ranking from point;", rankingByPointRowMapper());
    }

    public List<RankingCategory> findRankingByCategory(String categoryCode) {
        return jdbcTemplate.query("select category_code, user_id, geo_code, RANK() over (partition by category_code order by user_id desc) ranking from (select category_code, user_id, geo_code, count(*) from certification where category_code = \"" + categoryCode + "\" group by user_id) by_category_code;", rankingByCategoryRowMapper(categoryCode));
    }

    public List<RankingPoint> findPointRankingByGeoCode(String geoCode){
        return jdbcTemplate.query("select user_id, ranking, weekly_point from ranking_point where geo_code = " + geoCode + ";", pointRankingByGeoCodeRowMapper(geoCode));
    }


    private RowMapper<RankingPoint> rankingByPointRowMapper() {
        return (rs, rowNum) -> {
            RankingPoint rankingPoint = RankingPoint.builder().userId(rs.getInt("user_id")).ranking(rs.getInt("ranking")).weeklyPoint(rs.getInt("weekly_point")).geoCode(rs.getString("geo_code")).build();
            return rankingPoint;
        };
    }

    private RowMapper<RankingCategory> rankingByCategoryRowMapper(String categoryCode) {
        return (rs, rowNum) -> {
            RankingCategory rankingCategory = RankingCategory.builder().userId(rs.getInt("user_id")).ranking(rs.getInt("ranking")).geoCode(rs.getString("geo_code")).categoryCode(categoryCode).build();
            return rankingCategory;
        };
    }

    // Certification Like + 1
    public void plusLikeCount(int certificationId) {
        jdbcTemplate.update("update certification set like_count = like_count + 1 where certification_id = ?", certificationId);
    }
}
