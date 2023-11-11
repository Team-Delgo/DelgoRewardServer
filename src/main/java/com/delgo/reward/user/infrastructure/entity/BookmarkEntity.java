package com.delgo.reward.user.infrastructure.entity;

import com.delgo.reward.domain.common.BaseTimeEntity;
import com.delgo.reward.user.domain.Bookmark;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookmark")
public class BookmarkEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookmarkId;
    private Integer userId;
    private Integer mungpleId;
    private Boolean isBookmarked;

    public Bookmark toModel(){
        return Bookmark
                .builder()
                .bookmarkId(bookmarkId)
                .userId(userId)
                .mungpleId(mungpleId)
                .isBookmarked(isBookmarked)
                .build();
    }

    public static BookmarkEntity from(Bookmark bookmark){
        return BookmarkEntity
                .builder()
                .bookmarkId(bookmark.getBookmarkId())
                .userId(bookmark.getUserId())
                .mungpleId(bookmark.getMungpleId())
                .isBookmarked(bookmark.getIsBookmarked())
                .build();
    }
}
