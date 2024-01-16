package com.delgo.reward.cert.service;


import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.comm.ncp.geo.GeoDataService;
import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.code.domain.Code;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.mungple.service.MungpleService;
import com.delgo.reward.cert.controller.request.CertCreate;
import com.delgo.reward.cert.controller.request.CertUpdate;
import com.delgo.reward.cert.repository.CertRepository;
import com.delgo.reward.code.service.CodeService;
import com.delgo.reward.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class CertCommandService {
    private final CertRepository certRepository;
    private final CodeService codeService;
    private final GeoDataService geoDataService;
    private final MungpleService mungpleService;
    private final UserQueryService userQueryService;

    public Certification save(Certification certification) {
        return certRepository.save(certification);
    }

    public Certification create(CertCreate certCreate) {
        User user = userQueryService.getOneByUserId(certCreate.userId());
        String address = geoDataService.getReverseGeoData(certCreate.latitude(), certCreate.longitude());
        Code code = codeService.getGeoCodeByAddress(address);
        return certRepository.save(Certification.from(certCreate, address, code, user));
    }

    public Certification createByMungple(CertCreate certCreate) {
        User user = userQueryService.getOneByUserId(certCreate.userId());
        Mungple mungple = mungpleService.getOneByMungpleId(certCreate.mungpleId());
        return certRepository.save(Certification.from(certCreate, mungple, user));
    }

    public Certification update(CertUpdate certUpdate) {
        Certification cert = certRepository.findOneByCertificationId(certUpdate.certificationId())
                .orElseThrow(() -> new NotFoundDataException("[Certification] certificationId : " + certUpdate.certificationId()));
        return certRepository.save(cert.update(certUpdate));
    }

    public void changeIsCorrect(int certificationId, boolean isCorrect) {
        Certification cert = certRepository.findOneByCertificationId(certificationId)
                .orElseThrow(() -> new NotFoundDataException("[Certification] certificationId : " + certificationId));
        cert.setIsCorrect(isCorrect);
    }

    public void increaseCommentCount(int certificationId){
        Certification cert = certRepository.findOneByCertificationId(certificationId)
                .orElseThrow(() -> new NotFoundDataException("[Certification] certificationId : " + certificationId));
        cert.setCommentCount(cert.getCommentCount() + 1);
    }

    public void setPhotos(int certificationId, List<String> photos){
        Certification cert = certRepository.findOneByCertificationId(certificationId)
                .orElseThrow(() -> new NotFoundDataException("[Certification] certificationId : " + certificationId));
        cert.setPhotos(photos);
    }

    public void delete(int certificationId) {
        certRepository.deleteById(certificationId);
    }

    public void deleteByUserId(int userId) {
        certRepository.deleteByUserId(userId);
    }
}
