package com.delgo.reward.certification.service;

import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.service.port.ReactionRepository;
import com.delgo.reward.certification.domain.ReactionCode;
import com.delgo.reward.certification.domain.Reaction;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Builder
@RequiredArgsConstructor
public class ReactionService {
    private final ReactionRepository reactionRepository;

    /**
     * 생성
     */
    @Transactional
    public Reaction create(int userId, int certificationId, ReactionCode reactionCode) {
        return reactionRepository.save(Reaction.from(userId, certificationId, reactionCode));
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
        return reactionRepository.findByUserIdAndCertIdAndCode(userId, certificationId, reactionCode)
                .orElseThrow(() -> new NullPointerException("NOT FOUND REACTION userId: " + userId + ", certificationId : " + certificationId));
    }

    /**
     * [certId] 리스트 조회
     */
    public List<Reaction> getListByCertId(int certificationId) {
        return reactionRepository.findActiveListByCertId(certificationId);
    }

    /**
     * [List<Certification>] 리스트 조회
     */
    public Map<Integer,List<Reaction>> getMapByCertList(List<Certification> certList) {
        List<Integer> certIdList = certList.stream().map(Certification::getCertificationId).toList();
        List<Reaction> reactionList = reactionRepository.findActiveListByCertIdList(certIdList);
        return reactionList.stream().collect(Collectors.groupingBy(Reaction::getCertificationId));
    }

    /**
     * exist 여부 반환
     */
    public Boolean hasReaction(int userId, int certificationId, ReactionCode reactionCode) {
        return reactionRepository.existsReaction(userId, certificationId, reactionCode);
    }

    /**
     * [certId] 삭제
     */
    @Transactional
    public void deleteByCertId(int certificationId) {
        reactionRepository.deleteByCertId(certificationId);
    }
}
