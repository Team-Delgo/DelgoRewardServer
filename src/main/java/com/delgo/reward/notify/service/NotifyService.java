package com.delgo.reward.notify.service;

import com.delgo.reward.notify.domain.Notify;
import com.delgo.reward.notify.repository.NotifyRepository;
import com.delgo.reward.token.domain.NotifyType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotifyService {
    private final NotifyRepository notifyRepository;

    public Notify save(int userId, NotifyType notifyType, String notifyMsg){
        return notifyRepository.save(Notify.from(userId, notifyType, notifyMsg));
    }

    public List<Notify> getListByUserId(int userId){
        return notifyRepository.findListByUserIdOrderByCreateAtDesc(userId);
    }
}
