package com.delgo.reward.domain.like;

import lombok.Data;

import java.io.Serializable;

@Data
public class LikeListPK implements Serializable {
    private Integer userId; // 좋아요 누른 user Id
    private Integer certificationId; // 게시글 Id
}