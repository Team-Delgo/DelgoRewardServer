package com.delgo.reward.domain.user;

import com.delgo.reward.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookmarkId;
    private Integer userId;
    private Integer mungpleId;
    private Boolean isBookmarked;

    public Bookmark setIsBookmarkedReverse(){
        this.isBookmarked = !this.isBookmarked;
        return this;
    }
}
