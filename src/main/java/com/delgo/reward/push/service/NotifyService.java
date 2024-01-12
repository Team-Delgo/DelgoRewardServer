package com.delgo.reward.push.service;

import com.delgo.reward.push.domain.Notify;
import com.delgo.reward.push.repository.NotifyRepository;
import com.delgo.reward.push.domain.NotifyType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotifyService {
    private final NotifyRepository notifyRepository;

    public Notify create(int userId, String notifyMsg, NotifyType notifyType){
        return notifyRepository.save(Notify.from(userId, notifyMsg, notifyType));
    }

    public List<Notify> getListByUserId(int userId){
        return notifyRepository.findListByUserIdOrderByCreateAtDesc(userId);
    }
}
