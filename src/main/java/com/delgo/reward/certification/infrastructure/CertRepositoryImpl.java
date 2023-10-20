package com.delgo.reward.certification.infrastructure;

import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.service.port.CertRepository;
import com.delgo.reward.dto.comm.Page;
import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CertRepositoryImpl implements CertRepository {

    private final CertJpaRepository certJpaRepository;

    @Override
    public Page<Certification> findListByCondition(CertCondition certCondition) {
        org.springframework.data.domain.Page<Certification> page = certJpaRepository.findListByCondition(certCondition);
        return Page.<Certification>builder()
                .number(page.getNumber())
                .size(page.getSize())
                .last(page.isLast())
                .totalCount(page.getTotalElements())
                .content(page.getContent())
                .build();
    }

    @Override
    public Certification save(Certification certification) {
        return certJpaRepository.save(certification);
    }

    @Override
    public void deleteById(int certId) {
        certJpaRepository.deleteById(certId);
    }

    @Override
    public void deleteByUserId(int userId) {
        certJpaRepository.deleteByUserUserId(userId);
    }


    //------------------------------------------------------------


    @Override
    public Integer countOfCorrectByMungpleId(int mungpleId) {
        return certJpaRepository.countOfCorrectByMungpleId(mungpleId);
    }

    @Override
    public Optional<Certification> findByCertId(Integer certId) {
        return certJpaRepository.findByCertId(certId);
    }

    @Override
    public List<UserVisitMungpleCountDTO> findVisitTop3MungpleIdByUserId(int userId, Pageable pageable) {
        return certJpaRepository.findVisitTop3MungpleIdByUserId(userId, pageable);
    }

    @Override
    public List<MungpleCountDTO> countGroupedByMungpleId() {
        return certJpaRepository.countGroupedByMungpleId();
    }

    @Override
    public List<Certification> findCertByGeoCode(String geoCode, Pageable pageable) {
        return certJpaRepository.findCertByGeoCode(geoCode, pageable);
    }

    @Override
    public List<Certification> findCertByPGeoCode(String pGeoCode, Pageable pageable) {
        return certJpaRepository.findCertByPGeoCode(pGeoCode, pageable);
    }

    @Override
    public List<Certification> findCertByPGeoCodeExceptGeoCode(String pGeoCode, String geoCode, Pageable pageable) {
        return certJpaRepository.findCertByPGeoCodeExceptGeoCode(pGeoCode, geoCode, pageable);
    }
}
