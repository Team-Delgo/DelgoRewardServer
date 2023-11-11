package com.delgo.reward.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Bookmark {
    private Integer bookmarkId;
    private Integer userId;
    private Integer mungpleId;
    private Boolean isBookmarked;

    public static Bookmark from(){
        return Bookmark.builder()

                .build();
    }

    public void setIsBookmarkedReverse(){
        this.isBookmarked = !this.isBookmarked;
    }

}
