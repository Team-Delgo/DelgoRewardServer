package com.delgo.reward.user.service;

import com.delgo.reward.user.domain.BanList;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.repository.BanListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BanService {
    private final UserQueryService userQueryService;
    private final BanListRepository banListRepository;

    @Transactional
    public User ban(int userId, int banUserId){
        BanList banList = BanList.builder().userId(userId).banUserId(banUserId).build();
        banListRepository.save(banList);

        // 벤 당한 유저 반환
        return userQueryService.getOneByUserId(banUserId);
    }
}
