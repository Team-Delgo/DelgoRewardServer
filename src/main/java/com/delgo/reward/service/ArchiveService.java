package com.delgo.reward.service;

import com.delgo.reward.domain.Archive;
import com.delgo.reward.dto.MainAchievementsDTO;
import com.delgo.reward.repository.ArchiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    // Archive 등록
    public void registerWelcome(int userId) {
        Archive archive = Archive.builder()
                .achievementsId(10) // WELCOME 업적 ID (변경되면 같이 변경해주어야 함.)
                .userId(userId)
                .isMain(0)
                .build();

        archiveRepository.save(archive);
    }

    // Archive 수정
    public List<Archive> setMainArchive(MainAchievementsDTO dto) {
        List<Archive> archives = new ArrayList<>();
        if (dto.getFirst() != 0) archives.add(archiveRepository.findByUserIdAndAchievementsId(dto.getUserId(), dto.getFirst()).orElseThrow(() -> new NullPointerException("NOT FOUND ARCHIVE")));
        if (dto.getSecond() != 0) archives.add(archiveRepository.findByUserIdAndAchievementsId(dto.getUserId(), dto.getSecond()).orElseThrow(() -> new NullPointerException("NOT FOUND ARCHIVE")));
        if (dto.getThird() != 0) archives.add(archiveRepository.findByUserIdAndAchievementsId(dto.getUserId(), dto.getThird()).orElseThrow(() -> new NullPointerException("NOT FOUND ARCHIVE")));

        for (int i = 0; i < archives.size(); i++)
            archives.get(i).setIsMain(i + 1);

        return archiveRepository.saveAll(archives);
    }

    // userId로 Archive 조회
    public List<Archive> getArchiveByUserId(int userId) {
        return archiveRepository.findByUserId(userId);
    }

    // 해당 User의 대표 Achievements 초기화
    public void resetMainAchievements(int userId) {
        List<Archive> mainList = archiveRepository.findByUserIdAndIsMainNot(userId, 0);

        for (Archive archive : mainList) {
            log.info("archive : {}", archive);
            archive.setIsMain(0);
            archiveRepository.save(archive);
        }
    }
}
