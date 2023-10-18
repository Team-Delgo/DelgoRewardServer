package com.delgo.reward;

import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.domain.certification.Reaction;
import com.delgo.reward.domain.like.LikeList;
import com.delgo.reward.repository.LikeListRepository;
import com.delgo.reward.repository.ReactionRepository;
import com.delgo.reward.service.ReactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LikeToReactionMigration {
    @Autowired
    private LikeListRepository likeListRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private ReactionService reactionService;

    @Test
    public void Migration(){
        List<LikeList> likeList = likeListRepository.findAll();

        for(LikeList like: likeList) {
            if(like.isLike()){
                reactionService.reaction(like.getUserId(), like.getCertificationId(), ReactionCode.CUTE);
            }
        }
    }
}
