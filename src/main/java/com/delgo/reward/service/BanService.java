package com.delgo.reward.service;

import com.delgo.reward.domain.BanList;
import com.delgo.reward.repository.BanListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BanService {
    private final BanListRepository banListRepository;

    public void ban(int userId, int banUserId){
        BanList banList = BanList.builder().userId(userId).banUserId(banUserId).build();
        banListRepository.save(banList);
    }
}
