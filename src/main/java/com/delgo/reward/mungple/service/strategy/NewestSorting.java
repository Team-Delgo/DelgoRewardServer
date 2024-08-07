package com.delgo.reward.mungple.service.strategy;

import com.delgo.reward.bookmark.domain.Bookmark;
import com.delgo.reward.mungple.domain.Mungple;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class NewestSorting implements MungpleSortingStrategy {
    private final List<Bookmark> bookmarkList;

    public NewestSorting(List<Bookmark> bookmarkList){
        this.bookmarkList = bookmarkList;
    }
    @Override
    public List<Mungple> sort(List<Mungple> mungpleList) {
        // 1. 북마크를 등록일의 오래된 순으로 정렬하고, 그에 해당하는 Mungple ID들을 가져온다.
        List<Integer> sortedMungpleIds = bookmarkList.stream()
                .sorted(Comparator.comparing(Bookmark::getRegistDt).reversed())
                .map(Bookmark::getMungpleId)
                .toList();

        // 2. 가져온 Mungple 목록을 ID를 키로 하는 맵으로 변환.
        Map<Integer, Mungple> mungpleIdToMungpleMap = mungpleList.stream()
                .collect(Collectors.toMap(Mungple::getMungpleId, mungple -> mungple));

        // 3. 정렬된 북마크 순서에 따라 Mungple 정렬
        return sortedMungpleIds.stream()
                .map(mungpleIdToMungpleMap::get)
                .toList();
    }
}
