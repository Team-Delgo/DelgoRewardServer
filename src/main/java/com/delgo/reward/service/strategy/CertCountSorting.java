package com.delgo.reward.service.strategy;

import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CertCountSorting implements MungpleSortingStrategy {
    private final List<Mungple> mungpleList;
    private final  List<MungpleCountDTO> countByCert;

    public CertCountSorting(List<Mungple> mungpleList, List<MungpleCountDTO> countByCert){
        this.mungpleList = mungpleList;
        this.countByCert = countByCert;
    }

    @Override
    public List<Mungple> sort() {
        Map<Integer, Mungple> mungpleMap = mungpleList.stream()
                .collect(Collectors.toMap(
                        Mungple::getMungpleId, // 키로 사용할 함수
                        Function.identity()        // 값으로 사용할 함수
                ));

        return Stream.concat(
                        countByCert.stream()
                                .map(MungpleCountDTO::getMungpleId)
                                .map(mungpleMap::get), // 해당 부분에서 Null 생성될 수 있음.
                        mungpleList.stream())
                .filter(Objects::nonNull) // null 값 제거 // isActive = false인 멍플의 인증이 존재할 경우 NullException 발생
                .distinct()
                .toList();
    }
}