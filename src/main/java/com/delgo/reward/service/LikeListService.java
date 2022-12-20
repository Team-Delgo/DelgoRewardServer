package com.delgo.reward.service;




import com.delgo.reward.comm.fcm.FcmService;
import com.delgo.reward.domain.like.LikeList;
import com.delgo.reward.repository.LikeListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LikeListService {
    private final LikeListRepository likeListRepository;
    private final FcmService fcmService;

    public Optional<LikeList> getLike(int userId, int certificationId) {
        return likeListRepository.findByUserIdAndCertificationId(userId, certificationId);
    }

    // 특정 유저가 해당 게시글에 좋아요 눌렀는지 체크
    public boolean hasLiked(int userId, int certificationId) {
        return likeListRepository.findByUserIdAndCertificationId(userId, certificationId).isPresent();
    }

    public void register(int userId, int certificationId, int ownerId) throws IOException {
        likeListRepository.save(LikeList.builder()
                .userId(userId)
                .certificationId(certificationId)
                .build());
        fcmService.likePush(ownerId);
    }

    public void delete(LikeList likeList){
        likeListRepository.delete(likeList);
    }
}
