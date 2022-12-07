package com.delgo.reward.service;

import com.delgo.reward.domain.achievements.Archive;
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
    public Archive register(Archive archive) {
        return archiveRepository.save(archive);
    }

    // Archive List 등록
    public List<Archive> registerArchives(List<Archive> archives) {
        return archiveRepository.saveAll(archives);
    }

    // welcome 업적 등록
    public void registerWelcome(int userId) {
        Archive archive = Archive.builder()
                .achievementsId(1) // WELCOME 업적 ID (변경되면 같이 변경해주어야 함.)
                .userId(userId)
                .isMain(1)
                .build();

        archiveRepository.save(archive);
    }

    // userId로 Archive 조회
    public List<Archive> getArchiveByUserId(int userId) {
        return archiveRepository.findByUserId(userId);
    }
}
