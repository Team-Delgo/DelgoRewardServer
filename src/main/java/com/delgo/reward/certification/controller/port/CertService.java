package com.delgo.reward.certification.controller.port;


import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.domain.request.CertCreate;
import com.delgo.reward.certification.domain.request.CertUpdate;
import com.delgo.reward.dto.comm.PageCustom;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import java.util.List;


public interface CertService {
    Certification create(CertCreate certCreate);
    Certification createByMungple(CertCreate certCreate);
    Certification update(CertUpdate certUpdate);
    Certification getById(int certificationId);
    PageCustom<Certification> getListByCondition(CertCondition condition);
    void delete(int certificationId);
    List<UserVisitMungpleCountDTO> getVisitedMungpleIdListTop3ByUserId(int userId);
}
