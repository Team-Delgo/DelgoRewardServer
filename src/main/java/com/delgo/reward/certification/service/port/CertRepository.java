package com.delgo.reward.certification.service.port;

import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.dto.comm.PageCustom;
import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.dto.user.VisitCountDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CertRepository {
    PageCustom<Certification> findListByCondition(CertCondition certCondition);
    Optional<Certification> findByCertId(Integer certificationId);

    //-----------------------------------------------------------

    Certification save(Certification certification);
    void deleteById(int certificationId);
    void deleteByUserId(int userId);
    List<MungpleCountDTO> countGroupedByMungpleId();
    Integer countOfCorrectByMungpleId(int mungpleId);
    List<VisitCountDTO> findVisitCountDTOList(int userId, Pageable pageable);
}
