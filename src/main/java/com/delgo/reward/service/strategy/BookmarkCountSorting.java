package com.delgo.reward.service.strategy;

import com.delgo.reward.mongoDomain.mungple.Mungple;
import com.delgo.reward.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class BookmarkCountSorting implements MungpleSortingStrategy {
    private final BookmarkService bookmarkService;

    @Override
    public List<Mungple> sort(List<Mungple> mungpleList) {
        Map<Integer, Mungple> mungpleMap = mungpleList.stream()
                .collect(Collectors.toMap(Mungple::getMungpleId, Function.identity()));

        return Stream.concat(
                        bookmarkService.getCountMapByMungple().entrySet().stream()
                                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                                .map(Map.Entry::getKey)
                                .map((mungpleMap::get)), // 해당 부분에서 Null 생성될 수 있음.
                        mungpleList.stream())
                .filter(Objects::nonNull) // null 값 제거 // isActive = false인 멍플의 인증이 존재할 경우 NullException 발생
                .distinct()
                .toList();
    }
}
