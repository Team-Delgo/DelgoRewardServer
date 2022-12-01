package com.delgo.reward.service;

import com.delgo.reward.domain.Achievements;
import com.delgo.reward.domain.AchievementsCondition;
import com.delgo.reward.domain.Archive;
import com.delgo.reward.repository.AchievementsConditionRepository;
import com.delgo.reward.repository.AchievementsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AchievementsService {

    private final AchievementsRepository achievementsRepository;

    private final ArchiveService archiveService; // 사용자 획득 업적
    private final CertService certificationService; // 사용자 인증
    private final AchievementsConditionRepository achievementsConditionRepository; // 특정 업적 조건

    // 전체 Achievements 리스트 조회
    public List<Achievements> getAchievementsAll() {
        return achievementsRepository.findAll();
    }

    // Achievements ID 로 조회
    public Achievements getAchievementsById(int achievementsId) {
        return achievementsRepository.findByAchievementsId(achievementsId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND Achievements"));
    }

    // Achievements 등록
    public Achievements registerAchievements(Achievements achievements) {
        return achievementsRepository.save(achievements);
    }

    // 멍플 조건 체크 후 Achievements List 조회
    public List<Achievements> getAchievementsByIsMungple(boolean isMungple) {
        return achievementsRepository.findByIsMungple(isMungple);
    }

    // 달성한 업적 있는지 Check
    public List<Achievements> checkEarnAchievements(int userId, boolean isMungple) {
        List<Achievements> earnAchievementsList = new ArrayList<>(); // 획득한 업적 리스트

        List<Archive> archiveList = archiveService.getArchiveByUserId(userId); // 사용자 획득 업적 조회
        List<Achievements> achievementsList = getAchievementsByIsMungple(isMungple); // 일반 인증, 멍플 인증 구분해서 조회

        for (Archive archive : archiveList) // 사용자가 획득한 업적 삭제
            achievementsList.removeIf(a -> Objects.equals(a.getAchievementsId(), archive.getAchievementsId()));

        for (Achievements achievements : achievementsList) {
            List<AchievementsCondition> achievementsConditionList = achievementsConditionRepository.findByAchievementsId(achievements.getAchievementsId());
            boolean conditionCheck = true;
            for (AchievementsCondition ac : achievementsConditionList) {
                int certCount = (ac.getMungpleId() == 0) // ac.getMungpleId() == 0 -> 일반 인증 조건
                        ? certificationService.countCertByUserIdAndCategoryCode(userId, ac.getCategoryCode()) // 일반 인증 조건
                        : certificationService.countCertByUserIdAndCategoryCodeAndMungpleId(userId, ac.getCategoryCode(),ac.getMungpleId()); // 멍플 인증 조건

                if (ac.getCount() > certCount)  conditionCheck = false;
            }
            // 모든 조건을 만족하면 획득한 업적 리스트에 저장
            if(conditionCheck) earnAchievementsList.add(achievements);
        }
        return earnAchievementsList;
    }
}
