package com.delgo.reward.service;


import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.repository.MungpleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MungpleService {

    private final MungpleRepository mungpleRepository;

    // Mungple 등록
    public Mungple registerMungple(Mungple mungple) {
        return mungpleRepository.save(mungple);
    }

    // 전체 Mungple 리스트 조회
    public List<Mungple> getMungpleAll() {
        return mungpleRepository.findAll();
    }

    // mungpleId로 Mungple 조회
    public Mungple getMungpleByMungpleId(int mungpleId) {
        return mungpleRepository.findByMungpleId(mungpleId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE"));
    }

    // categoryCode로 Mungple 조회
    public List<Mungple> getMungpleByCategoryCode(String categoryCode) {
        return mungpleRepository.findByCategoryCode(categoryCode);
    }

    // 중복 체크
    public boolean isMungpleExisting(Location location) {
        Optional<Mungple> mungple = mungpleRepository.findByLatitudeAndLongitude(location.getLatitude(), location.getLongitude());
        return mungple.isPresent();
    }
}
