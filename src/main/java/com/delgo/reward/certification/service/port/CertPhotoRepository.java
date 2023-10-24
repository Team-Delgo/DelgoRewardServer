package com.delgo.reward.certification.service.port;

import com.delgo.reward.certification.domain.CertPhoto;

import java.util.List;

public interface CertPhotoRepository {
    List<CertPhoto> saveAll(List<CertPhoto> certPhotoList);
    List<CertPhoto> findListByCertId(int certificationId);
    List<CertPhoto> findListByCertIdList(List<Integer> certificationIdList);
}
