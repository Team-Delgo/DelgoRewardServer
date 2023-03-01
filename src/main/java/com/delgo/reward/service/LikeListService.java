package com.delgo.reward.service;


import com.delgo.reward.comm.fcm.FcmService;
import com.delgo.reward.domain.like.LikeList;
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
    private final FcmService fcmService;

    // 특정 유저가 해당 게시글에 좋아요 눌렀는지 체크
    public boolean hasLiked(int userId, int certificationId) {
        return likeListRepository.findByUserIdAndCertificationId(userId, certificationId).isPresent();
    }

    // 게시글 좋아요 개수 확인.
    public int getLikeCount(int certificationId) {
        return likeListRepository.countByCertificationId(certificationId);
    }

    public void like(int userId, int certificationId) {
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
