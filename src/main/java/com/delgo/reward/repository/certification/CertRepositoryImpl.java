package com.delgo.reward.repository.certification;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.comm.PageResDTO;
import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CertRepositoryImpl implements CertRepository {

    private final CertJpaRepository certJpaRepository;

    @Override
    public PageResDTO<Certification> findListByCondition(CertCondition certCondition) {
        Page<Certification> page = certJpaRepository.findListByCondition(certCondition);
        return PageResDTO.<Certification>builder()
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
