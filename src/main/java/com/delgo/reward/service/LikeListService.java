package com.delgo.reward.service;


import com.delgo.reward.comm.fcm.FcmService;
import com.delgo.reward.domain.like.LikeList;
import com.delgo.reward.repository.LikeListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LikeListService {
    public static ConcurrentHashMap<LikeList,Boolean> likeHashMap = new ConcurrentHashMap<>();

    private final LikeListRepository likeListRepository;
    private final FcmService fcmService;

    @PostConstruct
    public void init() {
        likeListRepository.findAll().forEach(like -> likeHashMap.put(like, true));
    }

    // 특정 유저가 해당 게시글에 좋아요 눌렀는지 체크
    public boolean hasLiked(int userId, int certificationId) {
        return likeHashMap.getOrDefault(new LikeList(userId, certificationId), false);
    }

    public void like(int userId, int certificationId, int ownerId) throws IOException {
        likeHashMap.put(new LikeList(userId, certificationId), true);
//        fcmService.likePush(ownerId);
    }

    public void unlike(int userId, int certificationId) {
        likeHashMap.put(new LikeList(userId, certificationId), false);
    }

    // HashMap에서 특정 User 관련 데이터 삭제.
    public void deleteUserRelatedLike(int userId) {
        likeHashMap.keySet().removeIf(like-> like.getUserId().equals(userId)); // HashMap Remove
        likeListRepository.deleteByUserId(userId); // DB Remove
    }

    // HashMap에서 특정 Certification 관련 데이터 삭제.
    public void deleteCertificationRelatedLike(int certificationId) {
        likeHashMap.keySet().removeIf(like-> like.getCertificationId().equals(certificationId)); // HashMap Remove
        likeListRepository.deleteByCertificationId(certificationId); // DB Remove
    }

    public int getLikeCount(int certificationId) {
        return (int) likeHashMap.entrySet().stream()
                .filter(e -> e.getKey().getCertificationId().equals(certificationId))
                .count();
    }

    public void updateDB(){
        List<LikeList> insertList = new ArrayList<>();
        List<LikeList> deleteList = new ArrayList<>();

        likeHashMap.forEach((like , isInsert) -> {
            if(isInsert) insertList.add(like);
            else deleteList.add(like);
        });

        likeListRepository.saveAll(insertList);
        likeListRepository.deleteAllInBatch(deleteList);
    }
}
