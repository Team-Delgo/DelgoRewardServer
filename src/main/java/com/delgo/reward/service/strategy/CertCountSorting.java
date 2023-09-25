package com.delgo.reward.service.strategy;

import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import java.util.List;
import java.util.Map;

public class CertCountSorting implements MungpleSortingStrategy {
    private final List<MongoMungple> mungpleList;
    private final Map<Integer,Integer> sortedMungpleId;

    public CertCountSorting(List<MongoMungple> mungpleList, Map<Integer,Integer> sortedMungpleId){
        this.mungpleList = mungpleList;
        this.sortedMungpleId = sortedMungpleId;
    }

    @Override
    public List<MongoMungple> sort() {
        mungpleList.sort((m1, m2) -> {
            int index1 = sortedMungpleId.getOrDefault(m1.getMungpleId(), -1);
            int index2 = sortedMungpleId.getOrDefault(m2.getMungpleId(), -1);

            if (index1 == index2) return 0; // 둘 다 없을 때 (-1 == -1)
            if (index1 == -1) return 1;     // m1이 없을 때
            if (index2 == -1) return -1;    // m2가 없을 때

            return Integer.compare(index1, index2);
        });

        return mungpleList;
    }
}