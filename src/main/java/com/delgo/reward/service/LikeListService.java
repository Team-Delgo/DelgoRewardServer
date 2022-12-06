package com.delgo.reward.service;




import com.delgo.reward.domain.LikeList;
import com.delgo.reward.repository.LikeListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LikeListService {
    private final LikeListRepository likeListRepository;

    // 특정 유저가 해당 게시글에 좋아요 눌렀는지 체크
    public boolean hasLiked(int userId, int certificationId) {
        return likeListRepository.findByUserIdAndCertificationId(userId, certificationId).isPresent();
    }

    public void register(int userId, int certificationId) {
        likeListRepository.save(LikeList.builder()
                .userId(userId)
                .certificationId(certificationId)
                .build());
    }

    public int delete(int userId,int certificationId){
        int test = likeListRepository.deleteByUserIdAndCertificationId(userId,certificationId);
        log.info("test : {}", test);
        return test;
    }
}
