package com.delgo.reward.certification.service;

import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.service.port.ReactionRepository;
import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.certification.domain.Reaction;
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

    /**
     * 생성
     */
    @Transactional
    public Reaction create(int userId, int certificationId, ReactionCode reactionCode) {
        return reactionRepository.save(Reaction.from(userId, certificationId, reactionCode));
    }

    /**
     * 수정
     */
    @Transactional
    public Reaction update(int userId, int certificationId, ReactionCode reactionCode) {
        Reaction reaction = reactionRepository.findReaction(userId, certificationId, reactionCode);
        reaction.update();

        return reactionRepository.save(reaction);
    }

    public List<Reaction> getListByCertId(int certificationId) {
        return reactionRepository.findActiveListByCertId(certificationId);
    }


    /**
     * [cert] 리스트 조회
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
