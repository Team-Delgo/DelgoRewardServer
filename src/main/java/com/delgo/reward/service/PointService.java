package com.delgo.reward.service;

import com.delgo.reward.domain.Point;
import com.delgo.reward.repository.JDBCTemplatePointRepository;
import com.delgo.reward.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final JDBCTemplatePointRepository jdbcTemplatePointRepository;

    public void updateAccumulatedPoint(int userId, int categoryPoint){
        jdbcTemplatePointRepository.updateAccumulatedPoint(userId, categoryPoint);
    }

    public void updateWeeklyPoint(int userId, int categoryPoint){
        jdbcTemplatePointRepository.updateWeeklyPoint(userId, categoryPoint);
    }

    public Point getPointByUserId(int userId){
        return pointRepository.findByUserId(userId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND POINT"));
    }
}
