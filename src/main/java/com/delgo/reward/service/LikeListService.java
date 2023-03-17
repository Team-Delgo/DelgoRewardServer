package com.delgo.reward.service;


import com.delgo.reward.comm.fcm.FcmService;
import com.delgo.reward.domain.like.LikeList;
import com.delgo.reward.domain.notify.NotifyType;
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
    private final UserService userService;
    private final NotifyService notifyService;

    // 특정 유저가 해당 게시글에 좋아요 눌렀는지 체크
    public boolean hasLiked(int userId, int certificationId) {
        return likeListRepository.findByUserIdAndCertificationId(userId, certificationId).isPresent();
    }

    // 게시글 좋아요 개수 확인.
    public int getLikeCount(int certificationId) {
        return likeListRepository.countByCertificationId(certificationId);
    }

    /**
     * 유저가 좋아요를 누르면 알림을 저장하고 좋아요 리스트 업데이트
     * @param userId
     * @param certificationId
     * @param ownerId
     */
    public void like(int userId, int certificationId, int ownerId) {
        String notifyMsg = userService.getUserById(userId).getName() + "님이 나의 게시글에 좋아요를 눌렀습니다.";
        notifyService.saveNotify(ownerId, NotifyType.LIKE, notifyMsg);

        likeListRepository.save(new LikeList(userId, certificationId));
    }

    public void unlike(int userId, int certificationId) {
        likeListRepository.deleteByUserIdAndCertificationId(userId, certificationId);
    }

    // 특정 User 관련 데이터 삭제.
    public void deleteUserRelatedLike(int userId) {
        likeListRepository.deleteByUserId(userId); // DB Remove
    }

    // 특정 Certification 관련 데이터 삭제.
    public void deleteCertificationRelatedLike(int certificationId) {
        likeListRepository.deleteByCertificationId(certificationId); // DB Remove
    }
}
