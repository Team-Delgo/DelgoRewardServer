package com.delgo.reward.service;


import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.repository.MungpleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MungpleService {

    private final MungpleRepository mungpleRepository;

    // Mungple 등록
    public Mungple register(Mungple mungple) {
        return  mungpleRepository.save(mungple);
    }

    // 전체 Mungple 리스트 조회
    public List<Mungple> getMungpleAll() {
        List<Mungple> mungpleList = mungpleRepository.findAllByIsActive(true);

        return mungpleList.stream().sorted(Comparator.comparing(Mungple::getCategoryCode)).collect(Collectors.toList());
    }

    // mungpleId로 Mungple 조회
    public Mungple getMungpleById(int mungpleId) {
        return mungpleRepository.findById(mungpleId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE"));
    }

    // categoryCode로 Mungple 조회
    public List<Mungple> getMungpleByCategoryCode(String categoryCode) {
        List<Mungple> mungpleList = mungpleRepository.findByCategoryCodeAndIsActive(categoryCode, true);

        return mungpleList.stream().sorted(Comparator.comparing(Mungple::getPlaceName)).collect(Collectors.toList());
    }

    // 중복 체크
    public boolean isMungpleExisting(Location location) {
        Optional<Mungple> mungple = mungpleRepository.findByLatitudeAndLongitude(location.getLatitude(), location.getLongitude());
        return mungple.isPresent();
    }

    // 멍플 주소 반환
    public String getMungpleAddress(int mungpleId){
        return getMungpleById(mungpleId).getRoadAddress();
    }
}
