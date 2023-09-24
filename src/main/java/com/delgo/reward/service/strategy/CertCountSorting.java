package com.delgo.reward.service.strategy;

import com.delgo.reward.domain.user.Bookmark;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.repository.CertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertCountSorting implements MungpleSortingStrategy {
    private final CertRepository certRepository;
    private final MongoMungpleRepository mongoMungpleRepository;

    @Override
    public List<MongoMungple> sort(List<MongoMungple> mungpleList) {
        // MariaDB에서 각 mungpleId별 Certification 개수 조회
        Map<MongoMungple, Integer> countMap = new HashMap<>();
        for(MongoMungple mungple : mungpleList) {
            int count = certRepository.countOfCorrectCertByMungple(mungple.getMungpleId());
            countMap.put(mungple, count);
        }

        // 인증 개수에 따라 mungpleId 리스트 정렬
        return mungpleList.stream()
                .sorted(Comparator.comparing(countMap::get).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<MongoMungple> sortByBookmark(List<Bookmark> bookmarkList) {
        List<Integer> mungpleIdList = bookmarkList.stream().map(Bookmark::getMungpleId).toList();
        List<MongoMungple> mungpleList = mongoMungpleRepository.findByMungpleIdIn(mungpleIdList);

        return sort(mungpleList);
    }
}
