package com.delgo.reward.mongoService;

import com.delgo.reward.certification.domain.ReactionCode;
import com.delgo.reward.mongoDomain.MongoCert;
import com.delgo.reward.mongoRepository.MongoCertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MongoCertReactionService {
    private final MongoCertRepository mongoCertRepository;

    public MongoCert reaction(int userId, String certId, ReactionCode reactionCode) {
        Optional<MongoCert> optionalCert= mongoCertRepository.findById(certId);
        if (optionalCert.isEmpty()) {
            return null;
        }
        MongoCert mongoCert = optionalCert.get();

        if (hasReaction(mongoCert, userId, reactionCode)) {
            // 이미 존재하면 취소
            System.out.println("[reaction] hasReaction EXIST: userId = " + userId + " certId = " + certId);
            mongoCert.getReactionUserList().get(reactionCode).remove(Integer.valueOf(userId));
            mongoCert.minusOneReactionCount(reactionCode);
        } else {
            // 없으면 추가
            System.out.println("[reaction] hasReaction NOT EXIST: userId = " + userId + " certId = " + certId);
            mongoCert.getReactionUserList().get(reactionCode).add(userId);
            mongoCert.addOneReactionCount(reactionCode);
        }

        return mongoCertRepository.save(mongoCert);
    }

    public Boolean hasReaction(MongoCert mongoCert, int userId, ReactionCode reactionCode) {

        List<Integer> reactionUserList = mongoCert.getReactionUserList().get(reactionCode);
        if (reactionUserList == null)
            return null;

        return reactionUserList.contains(userId);
    }
}
