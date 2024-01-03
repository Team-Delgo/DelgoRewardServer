package com.delgo.reward.mungple.service.strategy;

import com.delgo.reward.mungple.domain.Mungple;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class BookmarkCountSorting implements MungpleSortingStrategy {
    private final Map<Integer, Integer> countMap;

    public BookmarkCountSorting(Map<Integer, Integer> countMap){
        this.countMap = countMap;
    }

    @Override
    public List<Mungple> sort(List<Mungple> mungpleList) {
        Map<Integer, Mungple> mungpleMap = Mungple.listToMap(mungpleList);
        return Stream.concat(countMap.entrySet().stream()
                                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                                .map(Map.Entry::getKey)
                                .map((mungpleMap::get)), // 해당 부분에서 Null 생성될 수 있음.
                        mungpleList.stream())
                .filter(Objects::nonNull) // null 값 제거 // isActive = false인 멍플의 인증이 존재할 경우 NullException 발생
                .distinct()
                .toList();
    }
}
