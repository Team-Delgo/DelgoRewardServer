package com.delgo.reward.common.service;


import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.common.domain.Version;
import com.delgo.reward.common.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VersionService {
    private final VersionRepository versionRepository;

    public Version getVersion() {
        return versionRepository.findByIsActive(true)
                .orElseThrow(() -> new NotFoundDataException("[Version]"));
    }
}
