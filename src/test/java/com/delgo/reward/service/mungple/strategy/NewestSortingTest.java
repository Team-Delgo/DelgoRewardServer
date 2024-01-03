package com.delgo.reward.service.mungple.strategy;

import com.delgo.reward.bookmark.domain.Bookmark;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.mungple.service.strategy.NewestSorting;
import org.junit.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class NewestSortingTest {
    @Test
    public void sort() {
        // given
        Mungple mungple1 = Mungple.builder().mungpleId(1).build();
        Mungple mungple2 = Mungple.builder().mungpleId(2).build();
        Mungple mungple3 = Mungple.builder().mungpleId(3).build();

        Bookmark bookmark1 = Bookmark.builder().mungpleId(1).build();
        bookmark1.setRegistDt(LocalDateTime.of(2023,12,1,12,0,0));
        Bookmark bookmark2 = Bookmark.builder().mungpleId(2).build();
        bookmark2.setRegistDt(LocalDateTime.of(2023,12,5,12,0,0));
        Bookmark bookmark3 = Bookmark.builder().mungpleId(3).build();
        bookmark3.setRegistDt(LocalDateTime.of(2023,12,3,12,0,0));

        // when
        NewestSorting newestSorting = new NewestSorting(List.of(bookmark1, bookmark2, bookmark3));
        List<Mungple> sortedList = newestSorting.sort(List.of(mungple1, mungple2, mungple3));

        // then
        assertThat(sortedList).isEqualTo(List.of(mungple2, mungple3, mungple1));
    }
}