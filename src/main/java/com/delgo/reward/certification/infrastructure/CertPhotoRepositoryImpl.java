package com.delgo.reward.certification.infrastructure;

import com.delgo.reward.certification.domain.CertPhoto;
import com.delgo.reward.certification.infrastructure.entity.CertPhotoEntity;
import com.delgo.reward.certification.infrastructure.jpa.CertPhotoJpaRepository;
import com.delgo.reward.certification.service.port.CertPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CertPhotoRepositoryImpl implements CertPhotoRepository {
    private final CertPhotoJpaRepository certPhotoJpaRepository;

    @Override
    public List<CertPhoto> saveAll(List<CertPhoto> certPhotoList) {
        List<CertPhotoEntity> entityList = certPhotoList.stream().map(CertPhotoEntity::from).toList();
        return certPhotoJpaRepository.saveAll(entityList).stream().map(CertPhotoEntity::toModel).toList();
    }

    @Override
    public List<CertPhoto> findListByCertId(int certificationId) {
        List<CertPhotoEntity> entityList = certPhotoJpaRepository.findByCertificationId(certificationId);
        return entityList.stream().map(CertPhotoEntity::toModel).toList();
    }

    @Override
    public List<CertPhoto> findListByCertIdList(List<Integer> certificationIdList) {
        List<CertPhotoEntity> entityList = certPhotoJpaRepository.findByCertificationIdIn(certificationIdList);
        return entityList.stream().map(CertPhotoEntity::toModel).toList();
    }
}
