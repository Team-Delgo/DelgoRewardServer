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

    /**
     * Mungple 생성
     */
    public MungpleResDTO createMungple(MungpleRecord record, MultipartFile photo) {
        Location location = geoService.getGeoData(record.address()); // 위도, 경도

        Mungple mungple = mungpleRepository.save(record.toEntity(location));
        mungple.setPhotoUrl(photoService.uploadMungple(mungple.getMungpleId(), photo));

        return new MungpleResDTO(mungple);
    }

    /**
     * [mungpleId] Mungple 조회
     */
    public Mungple getMungpleById(int mungpleId) {
        return mungpleRepository.findById(mungpleId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE - mungpleId : " + mungpleId ));
    }

    /**
     * [categoryCode] Mungple 조회
     */
    public List<MungpleResDTO> getMungpleByCategoryCode(String categoryCode) {
        List<Mungple> mungpleList = !categoryCode.equals(CategoryCode.TOTAL.getCode())
                ? mungpleRepository.findMungpleByCategoryCode(categoryCode)
                : mungpleRepository.findAll();

        return mungpleList.stream().map(MungpleResDTO::new).collect(Collectors.toList());
    }

    /**
     * [categoryCode] Active Mungple 조회
     */
    public List<MungpleResDTO> getActiveMungpleByCategoryCode(String categoryCode) {
        List<Mungple> mungpleList = !categoryCode.equals(CategoryCode.TOTAL.getCode())
                ? mungpleRepository.findMungpleByCategoryCodeAndIsActive(categoryCode, true)
                : mungpleRepository.findMungpleByIsActive(true);

        return mungpleList.stream().map(MungpleResDTO::new).collect(Collectors.toList());
    }

    /**
     * [address] Mungple 중복 체크
     * NCP - 위도, 경도 구해야 함.
     */
    public boolean isMungpleExisting(String address) {
        Location location = geoService.getGeoData(address);
        return mungpleRepository.existsMungpleByLatitudeAndLongitude(location.getLatitude(), location.getLongitude());
    }

    /**
     * [인증 개수 많은 순] Mungple 조회
     */
    public List<MungpleResDTO> getMungpleOfMostCertCount(int count) {
        List<Integer> mungpleIds = certRepository.findCertOrderByMungpleCount(PageRequest.of(0, count));
        List<Mungple> mungpleList = mungpleRepository.findMungpleByIds(mungpleIds);

        return mungpleList.stream().map(MungpleResDTO::new).collect(Collectors.toList());
    }

    /**
     * Mungple Photo 수정
     */
    public Mungple modifyPhoto(int mungpleId, MultipartFile photo){
        return getMungpleById(mungpleId).setPhotoUrl(photoService.uploadMungple(mungpleId, photo));
    }

    /**
     * Mungple 삭제
     * NCP Object Storage 도 삭제 해줘야 함.
     */
    public void deleteMungple(int mungpleId){
        mungpleRepository.deleteById(mungpleId);
        objectStorageService.deleteObject(BucketName.MUNGPLE,mungpleId + "_mungple.webp"); // Thumbnail delete
        objectStorageService.deleteObject(BucketName.MUNGPLE_NOTE,mungpleId + "_mungplenote.webp"); // mungpleNote delete
    }
}