package com.delgo.reward.repository;

import com.delgo.reward.domain.ranking.RankingCategory;
import com.delgo.reward.domain.ranking.RankingPoint;
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

    public void setLastRanking(){
        jdbcTemplate.update("update ranking_point set last_ranking = ranking");
    }

    public List<RankingByPointDTO> findTopPointRankingByGeoCode(String geoCode){
        return jdbcTemplate.query("select u.user_id, ranking, weekly_point, profile, name from ranking_point natural join user u where geo_code = " + geoCode + " limit 10;", pointRankingByGeoCodeRowMapper(geoCode));
    }

    private RowMapper<RankingByPointDTO> pointRankingByGeoCodeRowMapper(String geoCode) {
        return (rs, rowNum) -> {
            RankingByPointDTO rankingByPointDTO = RankingByPointDTO.builder().userId(rs.getInt("user_id")).ranking(rs.getInt("ranking")).weeklyPoint(rs.getInt("weekly_point")).profile(rs.getString("profile")).name(rs.getString("name")).build();
            return rankingByPointDTO;
        };
    }

    public List<RankingPoint> findRankingByPoint() {
        return jdbcTemplate.query("select user_id, geo_code, weekly_point, RANK() over (partition by geo_code order by weekly_point desc) ranking from point;", rankingPointRowMapper());
    }

    public List<RankingCategory> findRankingByCategory(String categoryCode) {
        return jdbcTemplate.query("select category_code, user_id, geo_code, RANK() over (partition by category_code order by user_id desc) ranking from (select category_code, user_id, geo_code, count(*) from certification where category_code = \"" + categoryCode + "\" group by user_id) by_category_code;", rankingByCategoryRowMapper(categoryCode));
    }

    public List<RankingByPointDTO> findPointRankingByGeoCode(String geoCode){
        return jdbcTemplate.query("select user_id, ranking, weekly_point from ranking_point where geo_code = " + geoCode + ";", pointRankingByGeoCodeRowMapper(geoCode));
    }


    private RowMapper<RankingPoint> rankingPointRowMapper() {
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

    // Certification Like - 1
    public void minusLikeCount(int certificationId) {
        jdbcTemplate.update("update certification set like_count = like_count - 1 where certification_id = ?", certificationId);
    }

    // Certification Comment + 1
    public void plusCommentCount(int certificationId) {
        jdbcTemplate.update("update certification set comment_count = comment_count + 1 where certification_id = ?", certificationId);
    }
}
