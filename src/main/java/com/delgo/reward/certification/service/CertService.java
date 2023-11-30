package com.delgo.reward.certification.service;


import com.delgo.reward.certification.service.port.CertRepository;
import com.delgo.reward.common.domain.Code;
import com.delgo.reward.dto.user.VisitCountDTO;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.ncp.service.port.GeoDataPort;
import com.delgo.reward.ncp.domain.BucketName;
import com.delgo.reward.ncp.service.port.ObjectStoragePort;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.comm.PageCustom;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.certification.domain.request.CertCreate;
import com.delgo.reward.certification.domain.request.CertUpdate;
import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.common.service.CodeService;
import com.delgo.reward.service.UserService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;


@Builder
@Service
@RequiredArgsConstructor
public class CertService {

    private final UserService userService;
    private final CodeService codeService;
    private final MongoMungpleService mongoMungpleService;
    private final ObjectStoragePort objectStoragePort;

    private final CertRepository certRepository;
    private final MongoMungpleRepository mongoMungpleRepository;
    private final GeoDataPort geoDataPort;

    /**
     * DB에 저장
     */
    @Transactional
    public Certification save(Certification certification) {
        return certRepository.save(certification);
    }

    /**
     * 일반 인증 생성
     */
    @Transactional
    public Certification create(CertCreate certCreate) {
        return (certCreate.mungpleId() == 0) ? createByNormal(certCreate) : createByMungple(certCreate);
    }

    /**
     * 멍플 인증 생성
     */
    @Transactional
    public Certification createByMungple(CertCreate certCreate) {
        User user = userService.getUserById(certCreate.userId());
        MongoMungple mongoMungple = mongoMungpleService.getMungpleByMungpleId(certCreate.mungpleId());
        return save(Certification.from(certCreate, mongoMungple, user));
    }

    /**
     * 멍플 인증 생성
     */
    @Transactional
    public Certification createByNormal(CertCreate certCreate) {
        User user = userService.getUserById(certCreate.userId());
        String address = geoDataPort.getReverseGeoData(certCreate.latitude(), certCreate.longitude());
        Code geoCode = codeService.getGeoCodeByAddress(address);
        return save(Certification.from(certCreate, address, geoCode, user));
    }

    /**
     * [certId] 단 건 조회
     */
    public Certification getById(int certificationId) {
        return certRepository.findByCertId(certificationId)
                .orElseThrow(() -> new NoSuchElementException("[getById] NOT FOUND Certification Id : " + certificationId));
    }

    /**
     * [Condition] List 조회 - TODO Reaction 넣어 주는 작업 추가
     */
    public PageCustom<Certification> getListByCondition(CertCondition condition) {
        return certRepository.findListByCondition(condition);
    }

    /**
     * 인증 수정
     */
    @Transactional
    public Certification update(CertUpdate certUpdate) {
        Certification cert = getById(certUpdate.certificationId());
        return certRepository.save(cert.update(certUpdate));
    }

    /**
     * 인증 삭제
     */
    @Transactional
    public void delete(int certificationId) {
        certRepository.deleteById(certificationId);
        objectStoragePort.deleteObject(BucketName.CERTIFICATION,certificationId + "_cert.webp");
    }

    /**
     * 권한 인증
     */
    public Boolean validate(int userId, int certificationId) {
        int ownerId = getById(certificationId).getUser().getUserId();
        return Objects.equals(userId, ownerId);
    }
}
