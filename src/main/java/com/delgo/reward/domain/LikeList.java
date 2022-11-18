package com.delgo.reward.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer likeId; //
    private Integer userId; // 좋아요 누른 user Id
    private Integer certificationId; // 게시글 Id
}