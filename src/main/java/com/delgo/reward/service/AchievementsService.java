package com.delgo.reward.service;

import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.domain.achievements.AchievementsCondition;
import com.delgo.reward.domain.achievements.Archive;
import com.delgo.reward.dto.achievements.AchievementsDTO;
import com.delgo.reward.dto.achievements.MainAchievementsDTO;
import com.delgo.reward.repository.AchievementsRepository;
import com.delgo.reward.repository.CertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementsService {

    private final CertRepository certRepository;
    private final AchievementsRepository achievementsRepository;

    private final ArchiveService archiveService; // 사용자 획득 업적
    private final AchievementsConditionService achievementsConditionService; // 특정 업적 조건


    // Achievements 및 Condition 등록
    public Achievements registerWithCondition(AchievementsDTO dto) {
        String img; // 업적 이미지.
        switch (dto.getCategoryCode()) {
            case "CA0001": img = "https://kr.object.ncloudstorage.com/reward-achivements/%ED%98%95%ED%83%9C%3D%EC%B9%B4%EB%93%9C%2C%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%3D%EC%82%B0%EC%B1%85.png";break;
            case "CA0002": img = "https://kr.object.ncloudstorage.com/reward-achivements/%ED%98%95%ED%83%9C%3D%EC%B9%B4%EB%93%9C%2C%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%3D%EC%B9%B4%ED%8E%98.png";break;
            case "CA0003": img = "https://kr.object.ncloudstorage.com/reward-achivements/%ED%98%95%ED%83%9C%3D%EC%B9%B4%EB%93%9C%2C%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%3D%EC%8B%9D%EB%8B%B9.png";break;
            case "CA0004": img = "https://kr.object.ncloudstorage.com/reward-achivements/%ED%98%95%ED%83%9C%3D%EC%B9%B4%EB%93%9C%2C%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%3D%EB%AA%A9%EC%9A%95.png";break;
            case "CA0005": img = "https://kr.object.ncloudstorage.com/reward-achivements/%ED%98%95%ED%83%9C%3D%EC%B9%B4%EB%93%9C%2C%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%3D%EB%AF%B8%EC%9A%A9.png";break;
            case "CA0006": img = "https://kr.object.ncloudstorage.com/reward-achivements/%ED%98%95%ED%83%9C%3D%EC%B9%B4%EB%93%9C%2C%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%3D%EB%B3%91%EC%9B%90.png";break;
            default: img = ""; break;
        }

        // 업적 등록
        Achievements achievements =  achievementsRepository.save(dto.toEntity(img));
        // 조건 등록
        achievementsConditionService.register(AchievementsCondition.builder()
                .achievements(achievements)
                .categoryCode(dto.getCategoryCode())
                .count(dto.getCount())
                .mungpleId(0)
                .build()
        );
        return achievementsRepository.save(achievements);
    }

    // 전체 Achievements 리스트 조회
    public List<Achievements> getAchievementsAll() {
        return achievementsRepository.findAll();
    }

    // Achievements ID 로 조회
    public Achievements getAchievementsById(int achievementsId) {
        return achievementsRepository.findByAchievementsId(achievementsId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND Achievements"));
    }

    // 멍플 조건 체크 후 Achievements List 조회
    public List<Achievements> getAchievementsByIsMungple(boolean isMungple) {
        return achievementsRepository.findByIsMungple(isMungple);
    }

    // userId & categoryCode & mungple Id 만족하는 인증 개수
    public int getMungpleCategoryCount(int userId, String categoryCode, int mungpleId) {
        return certRepository.countByUserIdAndCategoryCodeAndMungpleId(userId, categoryCode, mungpleId);
    }

    // 달성한 업적 있는지 Check
    public List<Achievements> checkEarnAchievements(int userId, boolean isMungple) {
        List<Archive> archiveList = archiveService.getArchive(userId); // 사용자 획득 업적 조회
        List<Achievements> achievements = getAchievementsByIsMungple(isMungple); // 일반 인증, 멍플 인증 구분해서 조회

        for (Archive archive : archiveList) // 사용자가 획득한 업적 삭제
            achievements.removeIf(a -> Objects.equals(a.getAchievementsId(), archive.getAchievementsId()));

        // 획득한 업적 리스트
        return achievements.stream().map(achievement -> {
            achievement.getAchievementsCondition().forEach(ac -> { // ac.getMungpleId() == 0 -> 일반 인증 조건
                if (ac.getCount() > getMungpleCategoryCount(userId, ac.getCategoryCode(), ac.getMungpleId()))
                    achievement.setConditionCheck(false);
            });
            // 모든 조건을 만족하면 획득한 업적 리스트에 저장
            return (achievement.getConditionCheck()) ? achievement : null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    // Archive 수정
    public void setMainAchievements(MainAchievementsDTO dto) {
        // 대표 업적 초기화
        archiveService.resetMainArchive(dto.getUserId());

        List<Archive> archives = new ArrayList<>();
        if (dto.getFirst() != 0) archives.add(archiveService.getArchive(dto.getUserId(),dto.getFirst()).setMain(1));
        if (dto.getSecond() != 0) archives.add(archiveService.getArchive(dto.getUserId(),dto.getSecond()).setMain(2));
        if (dto.getThird() != 0) archives.add(archiveService.getArchive(dto.getUserId(),dto.getThird()).setMain(3));

        // 사용자 지정 업적으로 대표업적 설정
        archiveService.registerArchives(archives);
    }

    public List<Achievements> getAchievementsByUser(int userId){

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
}