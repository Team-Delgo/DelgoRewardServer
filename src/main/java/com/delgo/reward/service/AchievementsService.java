package com.delgo.reward.service;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.domain.achievements.AchievementsCondition;
import com.delgo.reward.domain.achievements.Archive;
import com.delgo.reward.record.achievements.AchievementsRecord;
import com.delgo.reward.record.achievements.MainAchievementsRecord;
import com.delgo.reward.repository.AchievementsRepository;
import com.delgo.reward.repository.CertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AchievementsService {

    // Repository
    private final CertRepository certRepository;
    private final AchievementsRepository achievementsRepository;

    // Service
    private final PhotoService photoService;
    private final ArchiveService archiveService; // 사용자 획득 업적
    private final ObjectStorageService objectStorageService;
    private final AchievementsConditionService achievementsConditionService; // 특정 업적 조건

    public Achievements save(Achievements achievements){
        return achievementsRepository.save(achievements);
    }

    // 업적 및 조건 삭제
    public void delete(int achievementId){
        Achievements achievements = getAchievements(achievementId);
        achievementsRepository.delete(achievements); // 업적 삭제
        achievementsConditionService.delete(achievements); // 업적 조건 삭제
        objectStorageService.deleteObject(BucketName.ACHIEVEMENTS, achievementId + "_achievements.webp");

    }

    public Achievements getAchievements(int achievementsId){
        return achievementsRepository.findById(achievementsId)
                .orElseThrow(() -> new NotFoundDataException("[Achievements] achievementsId : " + achievementsId));
    }

    // Achievements Condition 등록
    public Achievements registerWithCondition(AchievementsRecord record, MultipartFile photo) {
        // 업적 등록
        Achievements achievements =  achievementsRepository.save(record.toEntity());
        // 사진 등록
        String imgUrl = photoService.uploadAchievements(achievements.getAchievementsId(), photo);
        // 조건 등록
        achievementsConditionService.register(AchievementsCondition.builder()
                .achievements(achievements)
                .categoryCode(record.categoryCode())
                .count(record.count())
                .mungpleId(0)
                .build()
        );

        return achievements.setImg(imgUrl);
    }

    // 달성한 업적 있는지 Check
    public List<Achievements> checkEarnedAchievements(int userId, boolean isMungple) {
        return achievementsRepository.findAchievementsNotEarned(userId, isMungple).stream().map(achievement -> {
            achievement.getAchievementsCondition().forEach(ac -> { // ac.getMungpleId() == 0 -> 일반 인증 조건
                if (ac.getCount() > getCategoryCount(userId, CategoryCode.valueOf(ac.getCategoryCode()), ac.getMungpleId()))
                    achievement.setConditionCheck(false);
            });
            // 모든 조건을 만족하면 획득한 업적 리스트에 저장
            return (achievement.getConditionCheck()) ? achievement : null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<Achievements> getUserEarnedAchievements(int userId){

        List<Achievements> achievements = getAchievementsAll();
        List<Archive> archives = archiveService.getArchive(userId);

        achievements = achievements.stream().peek(achievement -> {
            archives.forEach(archive -> { // 유저 획득 업적
                if (Objects.equals(archive.getAchievementsId(), achievement.getAchievementsId()))
                    achievement.beActive(archive.getIsMain());
            });
        }).collect(Collectors.toList());

        // 정렬 코드
        List<Achievements> notMainAchievements = achievements.stream()
                .filter(a -> a.getIsMain() == 0)
                .sorted(Comparator.comparing(Achievements::getIsActive).thenComparing(Achievements::getRegistDt).reversed())
                .collect(Collectors.toList());
        achievements = achievements.stream()
                .filter(a -> a.getIsMain() != 0)
                .sorted(Comparator.comparing(Achievements::getIsMain))
                .collect(Collectors.toList());
        achievements.addAll(notMainAchievements);

        return achievements;
    }

    // 전체 Achievements 리스트 조회
    public List<Achievements> getAchievementsAll() {
        return achievementsRepository.findAll();
    }

    // 사용자가 특정 카테고리의 인증을 몇번했는지 조회
    public int getCategoryCount(int userId, CategoryCode categoryCode, int mungpleId) {
        return certRepository.countCertByCategory(userId, categoryCode, mungpleId);
    }

    // Archive 수정
    public void setMainAchievements(MainAchievementsRecord record) {
        // 대표 업적 초기화
        archiveService.resetMainArchive(record.userId());

        List<Archive> archives = new ArrayList<>();
        if (record.first() != 0) archives.add(archiveService.getArchive(record.userId(),record.first()).setMain(1));
        if (record.second() != 0) archives.add(archiveService.getArchive(record.userId(),record.second()).setMain(2));
        if (record.third() != 0) archives.add(archiveService.getArchive(record.userId(),record.third()).setMain(3));

        // 사용자 지정 업적으로 대표업적 설정
        archiveService.registerArchives(archives);
    }
}