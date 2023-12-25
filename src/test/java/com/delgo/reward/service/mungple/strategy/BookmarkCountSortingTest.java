package com.delgo.reward.service.mungple.strategy;

import com.delgo.reward.mongoDomain.mungple.Mungple;
import org.junit.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class BookmarkCountSortingTest {
    @Test
    public void sort() {
        // given
        Mungple mungple1 = Mungple.builder().mungpleId(1).build();
        Mungple mungple2 = Mungple.builder().mungpleId(2).build();
        Mungple mungple3 = Mungple.builder().mungpleId(3).build();

        Map<Integer, Integer> countMap = new HashMap<>();
        countMap.put(mungple1.getMungpleId(), 9);
        countMap.put(mungple2.getMungpleId(), 20);
        countMap.put(mungple3.getMungpleId(), 15);

        // when
        BookmarkCountSorting bookmarkCountSorting = new BookmarkCountSorting(countMap);
        List<Mungple> sortedList = bookmarkCountSorting.sort(List.of(mungple1, mungple2, mungple3));

        // then
        assertThat(sortedList).isEqualTo(List.of(mungple2, mungple3, mungple1));
    }
}