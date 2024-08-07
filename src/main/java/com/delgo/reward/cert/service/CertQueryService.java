package com.delgo.reward.cert.service;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.repository.dto.CategoryCountDTO;
import com.delgo.reward.cert.repository.dto.UserVisitMungpleCountDTO;
import com.delgo.reward.cert.repository.dto.MungpleCountDTO;
import com.delgo.reward.cert.repository.CertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
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

    public List<Certification> getListByPlaceName(String placeName) {
        return certRepository.findListByPlaceName(placeName);
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

    public List<UserVisitMungpleCountDTO> getVisitedMungpleIdListTop3ByUserId(int userId) {
        Pageable pageable = PageRequest.of(0, 3);
        return certRepository.findVisitTop3MungpleIdByUserId(userId, pageable);
    }

    public  Map<CategoryCode, Integer> getCategoryCountMapByUserId(int userId){
        Map<CategoryCode, Integer> categoryCountMap = certRepository.findCategoryCountGroupedByUserId(userId).stream()
                .collect(Collectors.toMap(CategoryCountDTO::getCategoryCode, CategoryCountDTO::getCount));

        Arrays.stream(CategoryCode.values())
                .filter(code -> !code.equals(CategoryCode.CA0000)) // CA0000 제외
                .forEach(code -> categoryCountMap.computeIfAbsent(code, k -> 0));
        return categoryCountMap;
    }
}
