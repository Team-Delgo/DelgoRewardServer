package com.delgo.reward.certification.infrastructure;

import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.infrastructure.entity.CertificationEntity;
import com.delgo.reward.certification.infrastructure.jpa.CertJpaRepository;
import com.delgo.reward.certification.service.port.CertRepository;
import com.delgo.reward.dto.comm.PageCustom;
import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.dto.user.VisitCountDTO;
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
    public PageCustom<Certification> findListByCondition(CertCondition certCondition) {
        Page<CertificationEntity> page = certJpaRepository.findListByCondition(certCondition);
        List<Certification> content = page.getContent().stream().map(CertificationEntity::toModel).toList();
        return PageCustom.<Certification>builder()
                .number(page.getNumber())
                .size(page.getSize())
                .last(page.isLast())
                .totalCount(page.getTotalElements())
                .content(content)
                .build();
    }

    @Override
    public Certification save(Certification certification) {
        return certJpaRepository.save(CertificationEntity.from(certification)).toModel();
    }

    @Override
    public void deleteById(int certificationId) {
        certJpaRepository.deleteById(certificationId);
    }

    @Override
    public void deleteByUserId(int userId) {
        certJpaRepository.deleteByUserUserId(userId);
    }


    @Override
    public Integer countOfCorrectByMungpleId(int mungpleId) {
        return certJpaRepository.countOfCorrectByMungpleId(mungpleId);
    }

    @Override
    public Optional<Certification> findByCertId(Integer certificationId) {
        return certJpaRepository.findByCertId(certificationId).map(CertificationEntity::toModel);
    }

    @Override
    public List<VisitCountDTO> findVisitCountDTOList(int userId, Pageable pageable) {
        return certJpaRepository.findVisitTop3MungpleIdByUserId(userId, pageable);
    }

    @Override
    public List<MungpleCountDTO> countGroupedByMungpleId() {
        return certJpaRepository.countGroupedByMungpleId();
    }
}
