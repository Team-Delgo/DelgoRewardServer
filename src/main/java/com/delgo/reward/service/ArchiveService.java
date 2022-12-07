package com.delgo.reward.service;

import com.delgo.reward.domain.achievements.Archive;
import com.delgo.reward.repository.ArchiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


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

    // userId로 Archive 조회
    public List<Archive> getArchive(int userId) {
        return archiveRepository.findByUserId(userId);
    }

    // userId & achievementsId로 Archive 조회
    public Archive getArchive(int userId, int achievementsId) {
        return archiveRepository.findByUserIdAndAchievementsId(userId, achievementsId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND ARCHIVE"));
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

    // 대표 업적 초기화
    public void resetMainArchive(int userId){
        archiveRepository.saveAll(archiveRepository.findByUserIdAndIsMainNot(userId, 0).stream()
                .map(archive -> archive.setMain(0))
                .collect(Collectors.toList())
        );
    }
}
