package com.delgo.reward.certification.service;


import com.delgo.reward.certification.controller.port.CertService;
import com.delgo.reward.certification.service.port.CertRepository;
import com.delgo.reward.certification.service.port.GeoDataPort;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.comm.PageCustom;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.certification.domain.request.CertCreate;
import com.delgo.reward.certification.domain.request.CertUpdate;
import com.delgo.reward.certification.infrastructure.jpa.CertPhotoJpaRepository;
import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.service.PhotoService;
import com.delgo.reward.service.UserService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Builder
@Service
@RequiredArgsConstructor
public class CertServiceImpl implements CertService {

    private final UserService userService;
    private final PhotoService photoService;
    private final MongoMungpleService mongoMungpleService;
    private final ObjectStorageService objectStorageService;

    private final CertRepository certRepository;
    private final CertPhotoJpaRepository certPhotoJpaRepository;

    private final GeoDataPort geoDataPort;

    /**
     * 인증 생성 (일반, 멍플 구분)
     */
    @Override
    @Transactional
    public Certification create(CertCreate certCreate) {
        // User 조회
        User user = userService.getUserById(certCreate.userId());
        return (certCreate.mungpleId() == 0)
                ? certRepository.save(Certification.from(certCreate, geoDataPort, user)) // 일반 인증
                : certRepository.save(Certification.from(certCreate, mongoMungpleService, user));  // 멍플 인증
    }

    /**
     * [certId] 단 건 조회
     */
    @Override
    public Certification getById(int certificationId) {
        return certRepository.findByCertId(certificationId);
    }

    /**
     * [Condition] List 조회 - TODO Reaction 넣어 주는 작업 추가
     */
    @Override
    public PageCustom<Certification> getListByCondition(CertCondition condition) {
        return certRepository.findListByCondition(condition);
    }

    /**
     * 인증 수정
     */
    @Override
    @Transactional
    public Certification update(CertUpdate certUpdate) {
        Certification cert = certRepository.findByCertId(certUpdate.certificationId());
        return certRepository.save(cert.update(certUpdate));
    }

    /**
     * 인증 삭제
     */
    @Override
    @Transactional
    public void delete(int certificationId) {
        certRepository.deleteById(certificationId);
        objectStorageService.deleteObject(BucketName.CERTIFICATION,certificationId + "_cert.webp");
    }


    /**
     * 유저 인증 중 가장 많이 방문한 멍플 조회
     */
    @Override
    public List<UserVisitMungpleCountDTO> getVisitedMungpleIdListTop3ByUserId(int userId){
        Pageable pageable = PageRequest.of(0, 3);

        List<UserVisitMungpleCountDTO> userVisitMungpleCountDTOList = certRepository.findVisitTop3MungpleIdByUserId(userId, pageable);
        return mongoMungpleService.getMungpleListByIds(userVisitMungpleCountDTOList);
    }
}
