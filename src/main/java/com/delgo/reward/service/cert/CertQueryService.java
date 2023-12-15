package com.delgo.reward.service.cert;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.repository.CertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CertQueryService {
    private final CertRepository certRepository;

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

    public Map<Integer, Integer> getCountMapByMungple(){
        return certRepository.countGroupedByMungpleId().stream()
                .collect(Collectors.toMap(MungpleCountDTO::getMungpleId, MungpleCountDTO::getCount));
    }

    public Integer getCountByMungpleId(int mungpleId){
        return certRepository.countOfCorrectCertByMungple(mungpleId);
    }
}
