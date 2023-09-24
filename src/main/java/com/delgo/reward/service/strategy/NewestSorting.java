package com.delgo.reward.service.strategy;

import com.delgo.reward.domain.user.Bookmark;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewestSorting implements MungpleSortingStrategy {
    private final MongoMungpleRepository mongoMungpleRepository;

    @Override
    public List<MongoMungple> sortByBookmark(List<Bookmark> bookmarkList) {
        // 1. 북마크를 등록일의 최신순으로 정렬하고, 그에 해당 하는 Mungple ID들을 가져온다.
        List<Integer> sortedMungpleIds = bookmarkList.stream()
                .sorted(Comparator.comparing(Bookmark::getRegistDt).reversed())
                .map(Bookmark::getMungpleId)
                .toList();

        // 2. ID 기반으로 MongoDB에서 해당 Mungple 목록을 가져온다.
        List<MongoMungple> mungpleList = mongoMungpleRepository.findByMungpleIdIn(sortedMungpleIds);

        // 3. 가져온 Mungple 목록을 ID를 키로 하는 맵으로 변환.
        Map<Integer, MongoMungple> mungpleIdToMungpleMap = mungpleList.stream()
                .collect(Collectors.toMap(MongoMungple::getMungpleId, mungple -> mungple));

        // 4. 정렬된 북마크 순서에 따라 Mungple 정렬
        return sortedMungpleIds.stream()
                .map(mungpleIdToMungpleMap::get)
                .toList();
    }

    @Override
    public List<MongoMungple> sort(List<MongoMungple> mungpleList) {
        return null;
    }

}
