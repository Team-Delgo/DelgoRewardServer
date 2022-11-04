package com.delgo.reward.service;


import com.delgo.reward.domain.Code;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;

    // categoryCode로 Mungple 조회
    public Code getGeoCodeBySIGUGUN(Location location) {
        // SIGUGUNS [codeName]만으로 조회시 중복 발생 ex) 중구
        // 따라서 pCode 조회 후 같이 조회
        Code code = getPCode(location.getSIDO());
        return codeRepository.findBypCodeAndCodeName(code.getPCode(), location.getSIGUGUN())
                .orElseThrow(() -> new NullPointerException("NOT FOUND GEOCODE"));
    }

    public Code getPCode(String SIDO) {
        return codeRepository.findByCodeName(SIDO)
                .orElseThrow(() -> new NullPointerException("NOT FOUND GEOCODE"));
    }

    // categoryCode로 Mungple 조회
    public Code getGeoCodeByCode(String code) {
        return codeRepository.findByCode(code)
                .orElseThrow(() -> new NullPointerException("NOT FOUND GEOCODE"));
    }

    public void registerCodeList(List<Code> codeList) {
        codeRepository.saveAll(codeList);
    }

    public List<Code> getCodeAll() {
       return codeRepository.findAll();
    }
}
