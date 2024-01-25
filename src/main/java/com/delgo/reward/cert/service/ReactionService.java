package com.delgo.reward.cert.service;

import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.domain.Reaction;
import com.delgo.reward.cert.repository.ReactionRepository;
import com.delgo.reward.comm.push.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReactionService {
    private final ReactionRepository reactionRepository;
    private final CertQueryService certQueryService;
    private final FcmService fcmService;

    /**
     * 생성
     */
    @Transactional
    public Reaction create(int userId, int certificationId, ReactionCode reactionCode) {
        Reaction reaction = reactionRepository.save(Reaction.from(userId, certificationId, reactionCode));
        Certification certification = certQueryService.getOneById(certificationId);

        if (reactionCode.equals(ReactionCode.HELPER)) {
            fcmService.helper(userId, certification.getUserId(), certificationId);
        } else {
            fcmService.cute(userId, certification.getUserId(), certificationId);
        }

        return reaction;
    }

    /**
     * isReaction 수정 (true -> false, false -> true)
     */
    @Transactional
    public Reaction update(int userId, int certificationId, ReactionCode reactionCode) {
        Reaction reaction = getByUserIdAndCertIdAndCode(userId, certificationId, reactionCode);
        reaction.update();

        return reactionRepository.save(reaction);
    }

    public Reaction getByUserIdAndCertIdAndCode(int userId, int certificationId, ReactionCode reactionCode){
        return reactionRepository.findByUserIdAndCertificationIdAndReactionCode(userId, certificationId, reactionCode)
                .orElseThrow(() -> new NullPointerException("NOT FOUND REACTION userId: " + userId + ", certificationId : " + certificationId));
    }

    /**
     * [certId] 리스트 조회
     */
    public List<Reaction> getListByCertId(int certificationId) {
        return reactionRepository.findByIsReactionAndCertificationId(true, certificationId);
    }

    /**
     * [List<Certification>] 리스트 조회
     */
    public Map<Integer,List<Reaction>> getMapByCertList(List<Certification> certList) {
        List<Integer> certIdList = certList.stream().map(Certification::getCertificationId).toList();
        List<Reaction> reactionList = reactionRepository.findByIsReactionAndCertificationIdIn(true, certIdList);
        return reactionList.stream().collect(Collectors.groupingBy(Reaction::getCertificationId));
    }

    /**
     * exist 여부 반환
     */
    public Boolean hasReaction(int userId, int certificationId, ReactionCode reactionCode) {
        return reactionRepository.existsByUserIdAndCertificationIdAndReactionCode(userId, certificationId, reactionCode);
    }

    /**
     * [certId] 삭제
     */
    @Transactional
    public void deleteByCertId(int certificationId) {
        reactionRepository.deleteByCertificationId(certificationId);
    }
}
