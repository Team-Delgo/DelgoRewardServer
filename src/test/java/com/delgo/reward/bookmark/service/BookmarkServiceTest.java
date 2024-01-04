package com.delgo.reward.bookmark.service;

import com.delgo.reward.bookmark.domain.Bookmark;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class BookmarkServiceTest {
    @Autowired
    BookmarkService bookmarkService;

    @Test
    @Transactional
    void bookmark() {
        // given
        int userId = 1;
        int mungpleId = 2;

        // when
        Bookmark bookmark = bookmarkService.bookmark(userId, mungpleId);

        // then
        assertThat(bookmark.getBookmarkId()).isGreaterThan(0);
        assertThat(bookmark.getUserId()).isEqualTo(userId);
        assertThat(bookmark.getMungpleId()).isEqualTo(mungpleId);
    }

    @Test
    void getListByUserId() {
        // given
        int userId = 1;

        // when
        List<Bookmark> bookmarkList = bookmarkService.getListByUserId(userId);

        // then
        assertThat(bookmarkList).extracting(bookmark -> bookmark.getUserId()).containsOnly(userId);
    }

    @Test
    void isBookmarked() {
        // given
        int userId = 1;
        int mungpleId = 1;

        // when
        boolean isBookmarked = bookmarkService.isBookmarked(userId, mungpleId);

        // then
        boolean expectedBookmarked = true;
        assertThat(isBookmarked).isEqualTo(expectedBookmarked);
    }

    @Test
    void getCountByMungpleId() {
        // given
        int mungpleId = 1;

        // when
        int count = bookmarkService.getCountByMungpleId(mungpleId);

        // then
        int expectedCount = 2;
        assertThat(count).isEqualTo(expectedCount);
    }

    @Test
    void getCountMapByMungple() {
        // given
        int mungpleId = 1;

        // when
        Map<Integer, Integer> map =  bookmarkService.getCountMapByMungple();

        // then
        int expectedCount = 2;
        assertThat(map.get(mungpleId)).isEqualTo(expectedCount);
    }
}