package com.delgo.reward.certification.controller.port;


import com.delgo.reward.certification.domain.CertPhoto;
import com.delgo.reward.certification.domain.Certification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


public interface CertPhotoService {
    List<CertPhoto> create(int certificationId, List<MultipartFile> photos);
    List<CertPhoto> getListByCertId(int certId);
    Map<Integer, List<CertPhoto>> getMapByCertList(List<Certification> certList);
}
