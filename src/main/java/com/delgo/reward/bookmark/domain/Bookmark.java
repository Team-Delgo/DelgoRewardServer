package com.delgo.reward.bookmark.domain;

import com.delgo.reward.common.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookmarkId;
    private Integer userId;
    private Integer mungpleId;
    private Boolean isBookmarked;

    public static Bookmark from(int userId, int mungpleId) {
        return Bookmark.builder()
                .userId(userId)
                .mungpleId(mungpleId)
                .isBookmarked(true)
                .build();
    }

    public Bookmark update() {
        return Bookmark.builder()
                .bookmarkId(bookmarkId)
                .userId(userId)
                .mungpleId(mungpleId)
                .isBookmarked(!isBookmarked)
                .build();
    }

    public static List<Integer> getMungpleIdList(List<Bookmark> bookmarkList){
        return bookmarkList.stream().map(Bookmark::getMungpleId).toList();
    }
}
