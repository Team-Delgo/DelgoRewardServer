package com.delgo.reward.fake;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.comm.PageResDTO;
import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import com.delgo.reward.repository.certification.CertCondition;
import com.delgo.reward.repository.certification.CertRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakeCertRepositoryImpl implements CertRepository {

    private final List<Certification> certList;

    public FakeCertRepositoryImpl(List<Certification> certList){
        this.certList = certList;
    }

    @Override
    public PageResDTO<Certification> findListByCondition(CertCondition certCondition) {
        System.out.println("certCondition = " + certCondition);
        Stream<Certification> stream = certList.stream()
                .filter(cert -> certCondition.getUserId() == 0 || certCondition.getUserId() == cert.getUser().getUserId())
                .filter(cert -> certCondition.getMungpleId() == 0 || certCondition.getMungpleId() == cert.getMungpleId())
                .filter(cert -> certCondition.getIsCorrect() == null || certCondition.getIsCorrect().equals(cert.getIsCorrect()))
                .filter(cert -> certCondition.getDate() == null || isSameDate(certCondition.getDate(), cert.getRegistDt()));
//                .sorted(Comparator.comparing(Certification::getRegistDt).reversed()); TODO 해결해야 함.

        List<Certification> filteredList = stream.collect(Collectors.toList());
        Page<Certification> page;
        if (certCondition.getPageable().isPaged()) {
            int start = (int) certCondition.getPageable().getOffset();
            int end = Math.min((start + certCondition.getPageable().getPageSize()), filteredList.size());

            List<Certification> pagedList = filteredList.subList(start, end);
            page = new PageImpl<>(pagedList, certCondition.getPageable(), filteredList.size());
        } else {
            page = new PageImpl<>(filteredList, certCondition.getPageable(), filteredList.size());
        }

        return PageResDTO.<Certification>builder()
                .number(page.getNumber())
                .size(page.getSize())
                .last(page.isLast())
                .totalCount(page.getTotalElements())
                .content(page.getContent())
                .build();
    }

    private boolean isSameDate(LocalDate date, LocalDateTime dateTime) {
        return date.equals(dateTime.toLocalDate());
    }



    @Override
    public Certification save(Certification certification) {
        return null;
    }

    @Override
    public void deleteById(int certId) {

    }

    @Override
    public void deleteByUserId(int userId) {

    }

    @Override
    public Integer countOfCorrectByMungpleId(int mungpleId) {
        return null;
    }

    @Override
    public Optional<Certification> findByCertId(Integer certId) {
        return Optional.empty();
    }

    @Override
    public List<UserVisitMungpleCountDTO> findVisitTop3MungpleIdByUserId(int userId, Pageable pageable) {
        return null;
    }

    @Override
    public List<MungpleCountDTO> countGroupedByMungpleId() {
        return null;
    }

    @Override
    public List<Certification> findCertByGeoCode(String geoCode, Pageable pageable) {
        return null;
    }

    @Override
    public List<Certification> findCertByPGeoCode(String pGeoCode, Pageable pageable) {
        return null;
    }

    @Override
    public List<Certification> findCertByPGeoCodeExceptGeoCode(String pGeoCode, String geoCode, Pageable pageable) {
        return null;
    }
}
