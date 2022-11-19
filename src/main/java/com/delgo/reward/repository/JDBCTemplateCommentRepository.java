package com.delgo.reward.repository;

import com.delgo.reward.dto.GetCommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JDBCTemplateCommentRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<GetCommentDTO> findCommentByCertificationId(int certificationId){
        return jdbcTemplate.query("select c.*, u.name, u.profile from comment c join user u on c.user_id = u.user_id where certification_id = " + certificationId + " and is_reply = false", CommentByCertificationRowMapper(certificationId));
    }

    private RowMapper<GetCommentDTO> CommentByCertificationRowMapper(int certificationId) {
        return (rs, rowNum) -> {
            GetCommentDTO getCommentDTO =
                    GetCommentDTO.builder().certificationId(rs.getInt("certification_id")).userId(rs.getInt("user_id")).userName(rs.getString("name")).content(rs.getString("content")).createDt(rs.getTimestamp("create_dt").toLocalDateTime()).profile(rs.getString("profile")).isReply(rs.getBoolean("is_reply")).build();

            return getCommentDTO;
        };
    }
}
