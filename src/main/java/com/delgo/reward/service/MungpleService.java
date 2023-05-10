package com.delgo.reward.service;


import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
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
    private final ObjectStorageService objectStorageService;

    // Mungple 등록
    public Mungple register(Mungple mungple) {
        return  mungpleRepository.save(mungple);
    }

    // 전체 Mungple 리스트 조회
    public List<Mungple> getMungpleAll() {
        List<Mungple> mungpleList = mungpleRepository.findAll();

        return mungpleList.stream().sorted(Comparator.comparing(Mungple::getCategoryCode)).collect(Collectors.toList());
    }

    // mungpleId로 Mungple 조회
    public Mungple getMungpleById(int mungpleId) {
        return mungpleRepository.findById(mungpleId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE"));
    }

    // categoryCode로 Mungple 조회
    public List<Mungple> getMungpleByCategoryCode(String categoryCode) {
        List<Mungple> mungpleList = mungpleRepository.findByCategoryCode(categoryCode);

        return mungpleList.stream().sorted(Comparator.comparing(Mungple::getPlaceName)).collect(Collectors.toList());
    }

    // categoryCode로 Mungple 조회
    public List<Mungple> getMungpleByMap() {
        List<Mungple> mungpleList = mungpleRepository.findAllByIsActive(true);

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

    // 인증 개수 많은 멍플 조회
    public List<Mungple> getMungpleOfMostCount(int count){
        return mungpleRepository.findMungpleOfMostCount(count);
    }

    // 멍플 삭제
    public void delete(int mungpleId){
        mungpleRepository.deleteById(mungpleId);
        objectStorageService.deleteObject(BucketName.MUNGPLE,mungpleId + "_mungple.webp"); // Thumbnail delete
        objectStorageService.deleteObject(BucketName.MUNGPLE_NOTE,mungpleId + "_mungplenote.webp"); // mungpleNote delete
    }

    public void deleteDuplicateMungple() {
        List<Mungple> mungples = mungpleRepository.findAll();
        log.info("count :{} ", mungples.size());
        int i = 1;
        for(Mungple mungple :mungples){
            List<Mungple> result = mungpleRepository.findByJibunAddress(mungple.getJibunAddress());
            if(result.size() >= 2) {
                log.info("{} 두개 이상 존재하는 result : {}", i++, result.get(1));
                mungpleRepository.deleteById(result.get(1).getMungpleId());

            }
        }
    }
}