package com.delgo.reward.mongoService;

import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.mongoDomain.MongoCert;
import com.delgo.reward.mongoRepository.MongoCertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoCertReactionService {
    private final MongoCertRepository mongoCertRepository;

    public MongoCert reaction(int userId, String certId, ReactionCode reactionCode) {
        if (mongoCertRepository.findById(certId).isEmpty()) {
            return null;
        }
        MongoCert mongoCert = mongoCertRepository.findById(certId).get();

        // 아예 null 이면 리스트 만들어서 추가
        if (hasReaction(userId, certId, reactionCode) == null) {
            System.out.println("[reaction] hasReaction NULL: userId = " + userId + " certId = " + certId);
            List<Integer> reactionUserList = new ArrayList<>();
            reactionUserList.add(userId);
            mongoCert.getReactionUserList().put(reactionCode, reactionUserList);
            mongoCert.addOneReactionCount(reactionCode);
        } else if (hasReaction(userId, certId, reactionCode)) {
            // 이미 존재하면 취소
            System.out.println("[reaction] hasReaction EXIST: userId = " + userId + " certId = " + certId);
            mongoCert.getReactionUserList().get(reactionCode).remove(Integer.valueOf(userId));
            mongoCert.minusOneReactionCount(reactionCode);
        } else if (!hasReaction(userId, certId, reactionCode)) {
            // 없으면 추가
            System.out.println("[reaction] hasReaction NOT EXIST: userId = " + userId + " certId = " + certId);
            mongoCert.getReactionUserList().get(reactionCode).add(userId);
            mongoCert.addOneReactionCount(reactionCode);
        }

        return mongoCertRepository.save(mongoCert);
    }

    public Boolean hasReaction(int userId, String certId, ReactionCode reactionCode) {

        MongoCert mongoCert = mongoCertRepository.findById(certId).get();
        List<Integer> reactionUserList = mongoCert.getReactionUserList().get(reactionCode);
        if (reactionUserList == null)
            return null;

        return reactionUserList.contains(userId);
    }
}
