package com.delgo.reward.domain.like;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Objects;

@Getter
@Entity
@Builder
@ToString
@IdClass(LikeListPK.class)
@NoArgsConstructor
@AllArgsConstructor
public class LikeList {
    @Id private Integer userId; // 좋아요 누른 user Id
    @Id private Integer certificationId; // 게시글 Id
    private boolean isLike; // True : like , False : unlike

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LikeList likeList = (LikeList) o;
        return userId != null && Objects.equals(userId, likeList.userId)
                && certificationId != null && Objects.equals(certificationId, likeList.certificationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, certificationId);
    }
}