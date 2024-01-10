package com.delgo.reward.comm.code;

import com.delgo.reward.cert.domain.Reaction;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@Schema(enumAsRef = true, description = """
        리액션 유형:
        * `HELPER` - 도움돼요
        * `CUTE` - 귀여워요
        """)
public enum ReactionCode {
    HELPER("HELPER", "도움돼요", "도움돼요😃를 받았어요", "님이 도움돼요😃를 남겼어요"),
    CUTE("CUTE", "귀여워요", "귀여워요😍를 받았어요", "님이 귀여워요😍를 남겼어요");

    private final String code;
    private final String desc;
    private final String pushTitle;
    private final String pushBody;

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
                reactionMap.put(reaction.getReactionCode(), reaction.getIsReaction());
            }
        }
    }

    public static void setReactionCountMap(Map<ReactionCode, Integer> reactionCountMap, List<Reaction> reactionList) {
        for(ReactionCode reactionCode: ReactionCode.values()){
            reactionCountMap.put(reactionCode, (int) reactionList.stream().filter(reaction -> reaction.getReactionCode().equals(reactionCode) && reaction.getIsReaction()).count());
        }
    }

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static ReactionCode from(String s) {
        return ReactionCode.valueOf(s);
    }
}
