package com.delgo.reward.service;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.record.mungple.MungpleRecord;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.repository.MungpleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MungpleService {

    // Service
    private final GeoService geoService;
    private final PhotoService photoService;
    private final ObjectStorageService objectStorageService;

    // Repository
    private final CertRepository certRepository;
    private final MungpleRepository mungpleRepository;

    // Mungple 등록
    public MungpleResDTO register(MungpleRecord record, MultipartFile photo) {
        Location location = geoService.getGeoData(record.address()); // 위도, 경도

        Mungple mungple = mungpleRepository.save(record.toEntity(location));
        mungple.setPhotoUrl(photoService.uploadMungple(mungple.getMungpleId(), photo));

        return new MungpleResDTO(mungple);
    }

    // mungpleId로 Mungple 조회
    public Mungple getMungpleById(int mungpleId) {
        return mungpleRepository.findById(mungpleId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE"));
    }

    // categoryCode로 Mungple 조회
    public List<MungpleResDTO> getMungpleByCategoryCode(String categoryCode) {
        List<Mungple> mungpleList = !categoryCode.equals(CategoryCode.TOTAL.getCode())
                ? mungpleRepository.findByCategoryCode(categoryCode)
                : mungpleRepository.findAll();

        return mungpleList.stream().map(MungpleResDTO::new).collect(Collectors.toList());
    }

    // categoryCode로 Active Mungple 조회
    public List<MungpleResDTO> getActiveMungpleByCategoryCode(String categoryCode) {
        List<Mungple> mungpleList = !categoryCode.equals(CategoryCode.TOTAL.getCode())
                ? mungpleRepository.findByCategoryCodeAndIsActive(categoryCode, true)
                : mungpleRepository.findAllByIsActive(true);

        return mungpleList.stream().map(MungpleResDTO::new).collect(Collectors.toList());
    }

    // categoryCode로 Mungple 조회
    public List<MungpleResDTO> getMungpleByMap(String categoryCode) {
        List<Mungple> mungpleList = !categoryCode.equals(CategoryCode.TOTAL.getCode())
                ? mungpleRepository.findByCategoryCodeAndIsActive(categoryCode,true)
                : mungpleRepository.findAllByIsActive(true);

        return mungpleList.stream().map(MungpleResDTO::new).collect(Collectors.toList());
    }

    // 중복 체크
    public boolean isMungpleExisting(String address) {
        Location location = geoService.getGeoData(address); // 위도, 경도
        return mungpleRepository.existsByLatitudeAndLongitude(location.getLatitude(), location.getLongitude());
    }

    // 인증 개수 많은 멍플 조회
    public List<Mungple> getMungpleOfMostCount(int count){
       List<Integer> mungpleIds = certRepository.findCertOrderByMungpleCount(PageRequest.of(0, count));
        return mungpleRepository.findMungpleByIds(mungpleIds);
    }

    // 멍플 삭제
    public void delete(int mungpleId){
        mungpleRepository.deleteById(mungpleId);
        objectStorageService.deleteObject(BucketName.MUNGPLE,mungpleId + "_mungple.webp"); // Thumbnail delete
        objectStorageService.deleteObject(BucketName.MUNGPLE_NOTE,mungpleId + "_mungplenote.webp"); // mungpleNote delete
    }

    // 멍플 사진 변경
    public Mungple changePhoto(int mungpleId, MultipartFile photo){
        return getMungpleById(mungpleId).setPhotoUrl(photoService.uploadMungple(mungpleId, photo));
    }
}