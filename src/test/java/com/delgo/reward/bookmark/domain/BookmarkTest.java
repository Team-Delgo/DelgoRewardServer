package com.delgo.reward.bookmark.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class BookmarkTest {
    @Test
    void from() {
        // given
        int userId = 1;
        int mungpleId = 1;

        // when
        Bookmark bookmark = Bookmark.from(userId, mungpleId);

        // then
        assertThat(bookmark.getUserId()).isEqualTo(userId);
        assertThat(bookmark.getMungpleId()).isEqualTo(mungpleId);
    }

    @Test
    void update() {
        // given
        Bookmark bookmark = Bookmark.builder()
                .bookmarkId(1)
                .isBookmarked(true)
                .build();

        // when
        Bookmark updatedBookmark = bookmark.update();

        // then
        assertThat(updatedBookmark.getBookmarkId()).isEqualTo(bookmark.getBookmarkId());
        assertThat(updatedBookmark.getIsBookmarked()).isEqualTo(!bookmark.getIsBookmarked());
    }

    @Test
    void getMungpleIdList() {
        // given
        List<Bookmark> bookmarkList = new ArrayList<>();
        bookmarkList.add(Bookmark.builder().mungpleId(1).build());
        bookmarkList.add(Bookmark.builder().mungpleId(2).build());
        bookmarkList.add(Bookmark.builder().mungpleId(3).build());

        // when
        List<Integer> mungpleIdList = Bookmark.getMungpleIdList(bookmarkList);

        // then
        assertThat(mungpleIdList.get(0)).isEqualTo(bookmarkList.get(0).getMungpleId());
        assertThat(mungpleIdList.get(1)).isEqualTo(bookmarkList.get(1).getMungpleId());
        assertThat(mungpleIdList.get(2)).isEqualTo(bookmarkList.get(2).getMungpleId());
    }
}