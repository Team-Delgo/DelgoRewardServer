package com.delgo.reward.certification.service;


import com.delgo.reward.certification.controller.port.CertService;
import com.delgo.reward.certification.service.port.CertRepository;
import com.delgo.reward.certification.service.port.GeoDataPort;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.domain.code.Code;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.comm.PageCustom;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.certification.domain.request.CertCreate;
import com.delgo.reward.certification.domain.request.CertUpdate;
import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.service.CodeService;
import com.delgo.reward.service.UserService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Builder
@Service
@RequiredArgsConstructor
public class CertServiceImpl implements CertService {

    private final UserService userService;
    private final CodeService codeService;
    private final MongoMungpleService mongoMungpleService;
    private final ObjectStorageService objectStorageService;

    private final CertRepository certRepository;
    private final GeoDataPort geoDataPort;

    /**
     * 일반 인증 생성
     */
    @Override
    @Transactional
    public Certification create(CertCreate certCreate) {
        User user = userService.getUserById(certCreate.userId());
        String address = geoDataPort.getReverseGeoData(certCreate.latitude(), certCreate.longitude());
        Code geoCode = codeService.getGeoByAddress(address);
        return certRepository.save(Certification.from(certCreate, address, geoCode, user));
    }

    /**
     * 멍플 인증 생성
     */
    @Override
    @Transactional
    public Certification createByMungple(CertCreate certCreate) {
        User user = userService.getUserById(certCreate.userId());
        MongoMungple mongoMungple = mongoMungpleService.getMungpleByMungpleId(certCreate.mungpleId());
        return certRepository.save(Certification.from(certCreate, mongoMungple, user));
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

    /**
     * 권한 인증
     */
    @Override
    public Boolean validate(int userId, int certificationId) {
        int ownerId = certRepository.findByCertId(certificationId).getUser().getUserId();
        return Objects.equals(userId, ownerId);
    }
}
