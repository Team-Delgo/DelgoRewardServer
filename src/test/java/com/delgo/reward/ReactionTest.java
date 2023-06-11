package com.delgo.reward;

import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.domain.certification.Reaction;
import com.delgo.reward.service.ReactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactionTest {
    @Autowired
    public ReactionService reactionService;

    @Test
    public void ReactionTest(){
        int userId = 279;
        int certId = 1275;
        ReactionCode reactionCode = ReactionCode.CUTE;
        Reaction reaction = reactionService.reaction(userId, certId, reactionCode);

        if (reaction != null){
            System.out.println(reaction.getReactionId());
            System.out.println(reaction.getUserId());
            System.out.println(reaction.getCertificationId());
            System.out.println(reaction.getReactionCode());
        }
    }
}
