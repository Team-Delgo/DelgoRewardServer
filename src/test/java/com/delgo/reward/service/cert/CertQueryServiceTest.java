package com.delgo.reward.service.cert;

import com.delgo.reward.cert.service.CertQueryService;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.repository.dto.UserVisitMungpleCountDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CertQueryServiceTest {
    @Autowired
    CertQueryService certQueryService;

    @Test
    public void getOneById() {
        // given
        int certificationId = 1;

        // when
        Certification certification = certQueryService.getOneById(certificationId);

        // then
        assertThat(certification.getCertificationId()).isEqualTo(certificationId);
    }

    @Test
    public void getListByDate() {
        // given
        int userId = 1;
        LocalDate localDate = LocalDate.of(2023,12,26);

        // when
        List<Certification> certificationList = certQueryService.getListByDate(userId, localDate);

        // then
        assertThat(certificationList).extracting(cert -> cert.getUser().getUserId()).containsOnly(userId);
        assertThat(certificationList).extracting(cert -> cert.getRegistDt().toLocalDate()).containsOnly(localDate);
    }

    @Test
    public void getListByPlaceName() {
        // given
        String placeName = "Test Place";

        // when
        List<Certification> certificationList= certQueryService.getListByPlaceName(placeName);

        // then
        assertThat(certificationList).extracting(cert -> cert.getPlaceName()).containsOnly(placeName);
    }

    @Test
    public void getPagingListByUserId() {
        // given
        int userId = 1;
        Pageable pageable = PageRequest.of(0,2);

        // when
        Page<Certification> certificationList = certQueryService.getPagingListByUserId(userId, pageable);

        // then
        assertThat(certificationList.getSize()).isEqualTo(pageable.getPageSize());
        assertThat(certificationList.getNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(certificationList).extracting(cert -> cert.getUser().getUserId()).containsOnly(userId);
    }

    @Test
    public void getPagingListByUserIdAndCategoryCode() {
        // given
        int userId = 1;
        Pageable pageable = PageRequest.of(0,2);
        CategoryCode categoryCode = CategoryCode.CA0002;

        // when
        Page<Certification> certificationList = certQueryService.getPagingListByUserIdAndCategoryCode(userId, categoryCode, pageable);

        // then
        assertThat(certificationList.getSize()).isEqualTo(pageable.getPageSize());
        assertThat(certificationList.getNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(certificationList).extracting(cert -> cert.getUser().getUserId()).containsOnly(userId);
        assertThat(certificationList).extracting(cert -> cert.getCategoryCode()).containsOnly(categoryCode);
    }

    @Test
    public void getCorrectPagingListByUserId() {
        // given
        int userId = 1;
        Pageable pageable = PageRequest.of(0,2);

        // when
        Page<Certification> certificationList = certQueryService.getCorrectPagingListByUserId(userId, pageable);

        // then
        assertThat(certificationList.getSize()).isEqualTo(pageable.getPageSize());
        assertThat(certificationList.getNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(certificationList).extracting(cert -> cert.getUser().getUserId()).containsOnly(userId);
        assertThat(certificationList).extracting(cert -> cert.getIsCorrect()).containsOnly(true);
    }

    @Test
    public void getCorrectPagingListByUserIdAndCategoryCode() {
        // given
        int userId = 1;
        Pageable pageable = PageRequest.of(0,2);
        CategoryCode categoryCode = CategoryCode.CA0002;

        // when
        Page<Certification> certificationList = certQueryService.getCorrectPagingListByUserIdAndCategoryCode(userId, categoryCode, pageable);

        // then
        assertThat(certificationList.getSize()).isEqualTo(pageable.getPageSize());
        assertThat(certificationList.getNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(certificationList).extracting(cert -> cert.getUser().getUserId()).containsOnly(userId);
        assertThat(certificationList).extracting(cert -> cert.getCategoryCode()).containsOnly(categoryCode);
        assertThat(certificationList).extracting(cert -> cert.getIsCorrect()).containsOnly(true);
    }

    @Test
    public void getCorrectPagingList() {
        // given
        int userId = 1;
        Pageable pageable = PageRequest.of(0,2);

        // when
        Page<Certification> certificationList = certQueryService.getCorrectPagingList(userId, pageable);

        // then
        assertThat(certificationList.getSize()).isEqualTo(pageable.getPageSize());
        assertThat(certificationList.getNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(certificationList).extracting(cert -> cert.getIsCorrect()).containsOnly(true);
        assertThat(certificationList).extracting(cert -> cert.getIsExpose()).containsOnly(true);
    }

    @Test
    public void getCorrectPagingListByMungpleId() {
        // given
        int userId = 1;
        int mungpleId = 1;
        Pageable pageable = PageRequest.of(0,2);

        // when
        Page<Certification> certificationList = certQueryService.getPagingListByMungpleId(userId, mungpleId, pageable);

        // then
        assertThat(certificationList.getSize()).isEqualTo(pageable.getPageSize());
        assertThat(certificationList.getNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(certificationList).extracting(cert -> cert.getMungpleId()).containsOnly(mungpleId);
        assertThat(certificationList).extracting(cert -> cert.getIsCorrect()).containsOnly(true);
    }

    @Test
    public void getCountByMungpleId() {
        // given
        int mungpleId = 1;

        // when
        int count = certQueryService.getCountByMungpleId(mungpleId);

        // then
        int expectedCount = 2;
        assertThat(count).isEqualTo(expectedCount);
    }

    @Test
    public void getCountMapByMungple() {
        // given
        int mungpleId = 1; // 니드스윗

        // when
        Map<Integer, Integer> countMap = certQueryService.getCountMapByMungple();

        // then
        int expectedCount = 2;
        assertThat(countMap.get(mungpleId)).isEqualTo(expectedCount);
    }

    @Test
    public void getVisitedMungpleIdListTop3ByUserId() {
        // given
        int userId = 1;

        // when
        List<UserVisitMungpleCountDTO> dtoList = certQueryService.getVisitedMungpleIdListTop3ByUserId(userId);

        // then
        int expectedMungpleId = 1;
        assertThat(dtoList.size()).isEqualTo(1);
        assertThat(dtoList).extracting(UserVisitMungpleCountDTO::getMungpleId).contains(expectedMungpleId);
    }
}