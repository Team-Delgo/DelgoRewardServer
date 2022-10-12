package com.delgo.reward.service;

import com.delgo.reward.repository.JDBCTemplatePointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PointService {

    private final JDBCTemplatePointRepository jdbcTemplatePointRepository;

    public void updateAccumulatedPoint(int userId, int categoryPoint){
        jdbcTemplatePointRepository.updateAccumulatedPoint(userId, categoryPoint);
    }

    public void updateWeeklyPoint(int userId, int categoryPoint){
        jdbcTemplatePointRepository.updateWeeklyPoint(userId, categoryPoint);
    }

}
