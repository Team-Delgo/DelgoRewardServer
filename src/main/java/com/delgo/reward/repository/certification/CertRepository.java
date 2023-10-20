package com.delgo.reward.repository.certification;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.comm.PageResDTO;
import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CertRepository {
    PageResDTO<Certification> findListByCondition(CertCondition certCondition);

    //-----------------------------------------------------------

    Certification save(Certification certification);

    void deleteById(int certId);

    void deleteByUserId(int userId);

    Integer countOfCorrectByMungpleId(@Param("mungpleId") int mungpleId);

    Optional<Certification> findByCertId(@Param("certId") Integer certId);

    List<UserVisitMungpleCountDTO> findVisitTop3MungpleIdByUserId(@Param("userId") int userId, Pageable pageable);

    List<MungpleCountDTO> countGroupedByMungpleId();

    List<Certification> findCertByGeoCode(@Param("geoCode") String geoCode, Pageable pageable);

    List<Certification> findCertByPGeoCode(@Param("pGeoCode") String pGeoCode, Pageable pageable);

    List<Certification> findCertByPGeoCodeExceptGeoCode(@Param("pGeoCode") String pGeoCode,
                                                        @Param("geoCode") String geoCode, Pageable pageable);
}
