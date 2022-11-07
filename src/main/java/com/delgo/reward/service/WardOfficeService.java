package com.delgo.reward.service;


import com.delgo.reward.domain.WardOffice;
import com.delgo.reward.repository.WardOfficeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WardOfficeService {

    private final WardOfficeRepository wardOfficeRepository;

    // WardOffice 등록
    public WardOffice registerWardOffice(WardOffice wardOffice) {
        return wardOfficeRepository.save(wardOffice);
    }

    // 전체 WardOffice 리스트 조회
    public List<WardOffice> getWardOfficeAll() {
        return wardOfficeRepository.findAll();
    }

    // GeoCode로 WardOffice 조회
    public WardOffice getWardOfficeByGeoCode(String geoCode) {
        return wardOfficeRepository.findByGeoCode(geoCode)
                .orElseThrow(() -> new NullPointerException("NOT FOUND WardOffice"));
    }

}
