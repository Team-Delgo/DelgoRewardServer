package com.delgo.reward.service;

import com.delgo.reward.domain.achievements.AchievementsCondition;
import com.delgo.reward.repository.AchievementsConditionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AchievementsConditionService {
    private final AchievementsConditionRepository achievementsConditionRepository; // 특정 업적 조건

    // AchievementsCondition 등록
    public AchievementsCondition register(AchievementsCondition achievementsCondition) {
        return achievementsConditionRepository.save(achievementsCondition);
    }
}
