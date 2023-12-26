package com.delgo.reward.service;

import com.delgo.reward.domain.BanList;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.repository.BanListRepository;
import com.delgo.reward.service.user.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BanService {
    private final UserQueryService userQueryService;
    private final BanListRepository banListRepository;

    public User ban(int userId, int banUserId){
        BanList banList = BanList.builder().userId(userId).banUserId(banUserId).build();
        banListRepository.save(banList);

        // 벤 당한 유저 반환
        return userQueryService.getUserById(banUserId);
    }
}
