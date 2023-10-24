package com.delgo.reward.certification.controller.port;

import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.domain.Reaction;
import com.delgo.reward.comm.code.ReactionCode;
import java.util.List;
import java.util.Map;


public interface ReactionService {
    Reaction create(int userId, int certificationId, ReactionCode reactionCode);
    Reaction update(int userId, int certificationId, ReactionCode reactionCode);
    List<Reaction> getListByCertId(int certificationId);
    Map<Integer,List<Reaction>> getMapByCertList(List<Certification> certList);
    Boolean hasReaction(int userId, int certificationId, ReactionCode reactionCode);
    void deleteByCertId(int certificationId);
}
