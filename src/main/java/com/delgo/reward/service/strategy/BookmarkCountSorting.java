package com.delgo.reward.service.strategy;

import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkCountSorting implements MungpleSortingStrategy {
    private final BookmarkRepository bookmarkRepository;

    @Override
    public List<MongoMungple> sort(List<MongoMungple> mungpleList) {
        // MariaDB에서 각 mungpleId별 Bookmark 개수 조회
        Map<MongoMungple, Integer> countMap = new HashMap<>();
        for(MongoMungple mungple : mungpleList) {
            int count = bookmarkRepository.countOfActiveBookmarkByMungple(mungple.getMungpleId());
            countMap.put(mungple, count);
        }

        // 저장 개수에 따라 mungpleId 리스트 정렬
        return mungpleList.stream()
                .sorted(Comparator.comparing(countMap::get).reversed())
                .collect(Collectors.toList());
    }
}
