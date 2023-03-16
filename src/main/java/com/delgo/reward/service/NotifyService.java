package com.delgo.reward.service;

import com.delgo.reward.domain.notify.Notify;
import com.delgo.reward.domain.notify.NotifyType;
import com.delgo.reward.repository.NotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotifyService {
    private final NotifyRepository notifyRepository;

    /**
     * 알림 저장
     * @param userId
     * @param notifyType
     * @param notifyMsg
     * @return 저장된 notify 반환
     */
    public Notify saveNotify(int userId, NotifyType notifyType, String notifyMsg){
        return notifyRepository.save(new Notify().toEntity(userId, notifyType, notifyMsg));
    }

    /**
     * 특정 유저의 모든 알림 조회
     * @param userId
     * @return 특정 유저의 모든 알림 반환
     */
    public List<Notify> getAllNotifyByUserId(int userId){
        return notifyRepository.findAllByUserIdOrderByCreateAtDesc(userId);
    }
}
