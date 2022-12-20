package com.delgo.reward.domain.like;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Getter
@Entity
@Builder
@IdClass(LikeListPK.class)
@NoArgsConstructor
@AllArgsConstructor
public class LikeList {
    @Id private Integer userId; // 좋아요 누른 user Id
    @Id private Integer certificationId; // 게시글 Id
}