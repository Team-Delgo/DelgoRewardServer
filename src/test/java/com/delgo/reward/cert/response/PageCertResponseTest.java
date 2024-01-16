package com.delgo.reward.cert.response;

import com.delgo.reward.cert.domain.Certification;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class PageCertResponseTest {

    @Test
    void from() {
        // given
        CertResponse certResponse1 = CertResponse.builder().certificationId(1).build();
        CertResponse certResponse2 = CertResponse.builder().certificationId(2).build();
        List<CertResponse> certResponseList = List.of(certResponse1, certResponse2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Certification> certificationPage = new PageImpl<>(new ArrayList<>(), pageRequest, certResponseList.size());

        // when
        PageCertResponse pageCertResponse = PageCertResponse.from(certificationPage, certResponseList);

        // then
        assertThat(pageCertResponse.getSize()).isEqualTo(pageRequest.getPageSize());
        assertThat(pageCertResponse.getNumber()).isEqualTo(pageRequest.getPageNumber());
        assertThat(pageCertResponse.isLast()).isEqualTo(certificationPage.isLast());
        assertThat(pageCertResponse.getTotalCount()).isEqualTo(certificationPage.getTotalElements());
        assertThat(pageCertResponse.getContent().size()).isEqualTo(certResponseList.size());
    }

    @Test
    void fromIncludeViewCount() {
        // given
        int viewCount = 2;
        CertResponse certResponse1 = CertResponse.builder().certificationId(1).build();
        CertResponse certResponse2 = CertResponse.builder().certificationId(2).build();
        List<CertResponse> certResponseList = List.of(certResponse1, certResponse2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Certification> certificationPage = new PageImpl<>(new ArrayList<>(), pageRequest, certResponseList.size());

        // when
        PageCertResponse pageCertResponse = PageCertResponse.from(certificationPage, certResponseList, viewCount);

        // then
        assertThat(pageCertResponse.getViewCount()).isEqualTo(viewCount);
    }
}