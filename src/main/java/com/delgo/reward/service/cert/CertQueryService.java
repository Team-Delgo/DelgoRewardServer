package com.delgo.reward.service.cert;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.repository.CertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CertQueryService {
    private final CertRepository certRepository;
    private final MongoMungpleService mongoMungpleService;

    public Certification getOneById(int certificationId) {
        return certRepository.findOneByCertificationId(certificationId)
                .orElseThrow(() -> new NotFoundDataException("[Certification] certificationId : " + certificationId));
    }

    public List<Certification> getListByDate(int userId, LocalDate date) {
        return certRepository.findListByDateAndUser(userId, date.atStartOfDay(), date.atTime(23, 59, 59));
    }

    public List<Certification> getListByDateWithoutUser(LocalDate localDate) {
        return certRepository.findListByDate(localDate.minusDays(1).atStartOfDay(), localDate.atStartOfDay());
    }

    public Page<Certification> getPagingListByUserId(int userId, Pageable pageable) {
        return certRepository.findPageByUserId(userId, pageable);
    }

    public Page<Certification> getPagingListByUserIdAndCategoryCode(int userId, CategoryCode categoryCode, Pageable pageable) {
        return certRepository.findPageByUserIdAndCategoryCode(userId, categoryCode, pageable);
    }

    public Page<Certification> getCorrectPagingListByUserId(int userId, Pageable pageable) {
        return certRepository.findCorrectPageByUserId(userId, pageable);
    }

    public Page<Certification> getCorrectPagingListByUserIdAndCategoryCode(int userId, CategoryCode categoryCode, Pageable pageable) {
        return certRepository.findCorrectPageByUserIdAndCategoryCode(userId, categoryCode, pageable);
    }

    public Page<Certification> getCorrectPagingList(int userId, Pageable pageable) {
        return certRepository.findCorrectPage(userId, pageable);
    }

    public Page<Certification> getPagingListByMungpleId(int userId, int mungpleId, Pageable pageable) {
         return certRepository.findCorrectPageByMungple(mungpleId, userId, pageable);
    }

    public List<UserVisitMungpleCountDTO> getVisitedMungpleIdListTop3ByUserId(int userId) {
        Pageable pageable = PageRequest.of(0, 3);

        List<UserVisitMungpleCountDTO> userVisitMungpleCountDTOList =
                certRepository.findVisitTop3MungpleIdByUserId(userId, pageable);
        return mongoMungpleService.getMungpleListByIds(userVisitMungpleCountDTOList);
    }
}
