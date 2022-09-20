package com.delgo.reward.service;

import com.delgo.reward.domain.Achievements;
import com.delgo.reward.repository.AchievementsConditionRepository;
import com.delgo.reward.repository.AchievementsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AchievementsService {

    private final AchievementsRepository achievementsRepository;

    // 전체 Achievements 리스트 조회
    public List<Achievements> getAchievementsAll() {
        return achievementsRepository.findAll();
    }

    // 전체 Achievements 리스트 조회
    public Achievements registerAchievements(Achievements achievements) {
        return achievementsRepository.save(achievements);
    }

    // 멍플 조건 체크 후 Achievements List 조회
    public List<Achievements> getAchievementsByIsMungple(int isMungple) {
        return achievementsRepository.findByIsMungple(isMungple);
    }
}
