package com.delgo.reward.repository;

import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.domain.certification.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    List<Reaction> findByCertificationId(int certId);
    List<Reaction> findByUserId(int userId);
    void deleteByUserIdAndCertificationIdAndReactionCode(int userId, int certId, ReactionCode reactionCode);
}
