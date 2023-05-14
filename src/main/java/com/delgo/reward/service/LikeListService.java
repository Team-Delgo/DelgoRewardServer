package com.delgo.reward.service;


import com.delgo.reward.comm.fcm.FcmService;
import com.delgo.reward.domain.like.LikeList;
import com.delgo.reward.domain.notify.NotifyType;
import com.delgo.reward.repository.LikeListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class LikeListService {
    private final LikeListRepository likeListRepository;

    private final FcmService fcmService;
    private final UserService userService;
    private final NotifyService notifyService;

    // 좋아요
    public void like(int userId, int certificationId, int ownerId) throws IOException {
        if (hasLiked(userId, certificationId)) { // 이미 기존의 좋아요 Data가 존재할 경우
            updateIsLike(userId, certificationId);
        } else {// 첫 좋아요 눌렀을 경우
            firstLike(userId, certificationId);
            if (userId != ownerId) { // 자신이 누른 좋아요 는 알람 보내지 않는다.
                String notifyMsg = userService.getUserById(userId).getName() + "님이 회원님의 게시물을 좋아합니다.";
                notifyService.saveNotify(ownerId, NotifyType.LIKE, notifyMsg);
                fcmService.likePush(ownerId, notifyMsg);
            }
        }
    }

    // 특정 유저가 해당 게시글에 좋아요 눌렀는지 체크
    public boolean hasLiked(int userId, int certificationId) {
        return likeListRepository.findByUserIdAndCertificationId(userId, certificationId).isPresent();
    }

    // 게시글 좋아요 개수 확인.
    public int getLikeCount(int certificationId) {
        return likeListRepository.countByCertificationIdAndIsLike(certificationId);
    }

    // 첫 Like 생성
    public void firstLike(int userId, int certificationId){
        likeListRepository.save(new LikeList(userId, certificationId, true));
    }

    // Like Update
    @Transactional
    public void updateIsLike(int userId, int certificationId) {
        likeListRepository.updateIsLike(userId,certificationId);
    }

    // 특정 Certification 관련 데이터 삭제.
    public void deleteCertificationRelatedLike(int certificationId) {
        likeListRepository.deleteByCertificationId(certificationId); // DB Remove
    }
}
