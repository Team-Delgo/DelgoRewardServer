package com.delgo.reward.service.mungple.strategy;

import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.mungple.service.strategy.DistanceSorting;
import org.junit.Test;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class DistanceSortingTest {
    @Test
    public void sort() {
        // given
        String latitude = "37.7749"; // 기준 위도
        String longitude = "127.1091707"; // 기준 경도
        Mungple mungple1 = Mungple.builder().mungpleId(1).location(new GeoJsonPoint(127.1091707, 37.5101562)).build();
        Mungple mungple2 = Mungple.builder().mungpleId(2).location(new GeoJsonPoint(127.1091707, 40.4999429)).build();
        Mungple mungple3 = Mungple.builder().mungpleId(3).location(new GeoJsonPoint(127.1091707, 38.4999429)).build();

        // when
        DistanceSorting distanceSorting = new DistanceSorting(latitude, longitude);
        List<Mungple> sortedList = distanceSorting.sort(List.of(mungple1, mungple2, mungple3));

        // then
        assertThat(sortedList).isEqualTo(List.of(mungple1, mungple3, mungple2));
    }
}