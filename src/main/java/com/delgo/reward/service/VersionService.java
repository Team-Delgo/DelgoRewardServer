package com.delgo.reward.service;


import com.delgo.reward.domain.Version;
import com.delgo.reward.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class VersionService {

    private final VersionRepository versionRepository;

    public Version getVersion() {
        return versionRepository.findByIsActive(true)
                .orElseThrow(() -> new NullPointerException("NOT FOUND VERSION"));
    }
}
