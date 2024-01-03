package com.delgo.reward.service.mungple.strategy;

import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.mungple.service.strategy.NotSorting;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NotSortingTest {

    @Test
    public void sort() {
        // given
        List<Mungple> mungpleList = new ArrayList<>();
        mungpleList.add(Mungple.builder().mungpleId(1).build());
        mungpleList.add(Mungple.builder().mungpleId(2).build());
        mungpleList.add(Mungple.builder().mungpleId(3).build());

        // when
        NotSorting notSorting = new NotSorting();
        List<Mungple> sortedList = notSorting.sort(mungpleList);

        // then
        assertThat(sortedList).isEqualTo(mungpleList);
    }
}