package com.delgo.reward.service;

import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.comm.fcm.FcmService;
import com.delgo.reward.domain.certification.Reaction;
import com.delgo.reward.domain.notify.NotifyType;
import com.delgo.reward.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReactionService {
    private final ReactionRepository reactionRepository;

    private final FcmService fcmService;
    private final CertService certService;
    private final UserService userService;
    private final NotifyService notifyService;

    /**
     * [Reaction] 리액션 등록 또는 제거
     */
    public Reaction reaction(int userId, int certId, ReactionCode reactionCode) throws IOException {
        if (hasReaction(userId, certId, reactionCode)) { // 이미 기존의 리액션 Data가 존재할 경우
            return getReaction(userId, certId, reactionCode).setIsReactionReverse();
        } else {
            int ownerId = certService.getCertById(certId).getUser().getUserId();
            if (userId != ownerId) { // 자신의 반응 에는 알람 X
                String notifyMsg = userService.getUserById(userId).getName() + "님이 회원님의 게시물에 반응했습니다.";
                NotifyType notifyType = (reactionCode.equals(ReactionCode.CUTE)) ? NotifyType.CUTE : NotifyType.HELPER;
                notifyService.saveNotify(ownerId, notifyType, notifyMsg);
                fcmService.likePush(ownerId, notifyMsg);
            }
            return reactionRepository.save(Reaction.builder()
                    .userId(userId)
                    .certificationId(certId)
                    .reactionCode(reactionCode)
                    .isReaction(true)
                    .build());
        }
    }

    /**
     * [Reaction] 리액션 존재 여부 반환
     */
    public Boolean hasReaction(int userId, int certId, ReactionCode reactionCode) {
        return reactionRepository.existsByUserIdAndCertificationIdAndReactionCode(userId, certId, reactionCode);
    }

    /**
     * [Reaction] 리액션 가져오기
     */
    public Reaction getReaction(int userId, int certId, ReactionCode reactionCode) {
        return reactionRepository.findByUserIdAndCertificationIdAndReactionCode(userId, certId, reactionCode)
                .orElseThrow(() -> new NullPointerException("NOT FOUND Reaction userId : " + userId + " certificationId: " + certId + " reactionCode: " + reactionCode.getCode()));
    }
}
