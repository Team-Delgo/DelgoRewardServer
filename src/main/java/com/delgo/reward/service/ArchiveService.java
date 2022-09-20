package com.delgo.reward.service;

import com.delgo.reward.domain.Archive;
import com.delgo.reward.repository.ArchiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArchiveService {
    private final ArchiveRepository archiveRepository;

    // Archive 등록
    public Archive registerArchive(Archive archive) {
        return archiveRepository.save(archive);
    }

    // userId로 Archive 조회
    public List<Archive> getArchiveByUserId(int userId) {
        return archiveRepository.findByUserId(userId);
    }
}
