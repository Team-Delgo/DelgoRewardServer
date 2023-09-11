package com.delgo.reward.comm.code;

import com.delgo.reward.domain.certification.Reaction;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ReactionCode {
    HELPER("HELPER", "도움돼요"),
    CUTE("CUTE", "귀여워요");

    private final String code;
    private final String desc;

    ReactionCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public static Map<ReactionCode, Boolean> initializeReactionMap() {
        Map<ReactionCode, Boolean> reactionMap = new HashMap<>();

        for (ReactionCode reactionCode : ReactionCode.values()) {
            reactionMap.put(reactionCode, false);
        }

        return reactionMap;
    }

    public static Map<ReactionCode, Integer> initializeReactionCountMap() {
        Map<ReactionCode, Integer> reactionCountMap = new HashMap<>();
        for (ReactionCode reactionCode : ReactionCode.values()) {
            reactionCountMap.put(reactionCode, 0);
        }
        return reactionCountMap;
    }

    public static void setReactionMapByUserId(Map<ReactionCode, Boolean> reactionMap, List<Reaction> reactionList, int userId) {
        for (Reaction reaction : reactionList) {
            if (reaction.getUserId().equals(userId)) {
                reactionMap.put(reaction.getReactionCode(), reaction.isReaction());
            }
        }
    }

    public static void setReactionCountMap(Map<ReactionCode, Integer> reactionCountMap, List<Reaction> reactionList) {
        for(ReactionCode reactionCode: ReactionCode.values()){
            reactionCountMap.put(reactionCode, (int) reactionList.stream().filter(reaction -> reaction.getReactionCode().equals(reactionCode) && reaction.isReaction()).count());
        }
    }

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static ReactionCode from(String s) {
        return ReactionCode.valueOf(s);
    }
}
