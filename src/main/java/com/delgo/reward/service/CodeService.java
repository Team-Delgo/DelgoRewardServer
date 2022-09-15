package com.delgo.reward.service;


import com.delgo.reward.domain.Code;
import com.delgo.reward.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;

    // categoryCode로 Mungple 조회
    public Code getGeoCodeBySIGUGUN(String SIGUGUN) {
        return codeRepository.findByCodeName(SIGUGUN)
                .orElseThrow(() -> new NullPointerException("NOT FOUND GEOCODE"));
    }
}
