package com.delgo.reward.token.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Schema(enumAsRef = true, description = """
        Push 카테고리:
        * `Reaction` - 리액션
        * `Comment` - 댓글
        * `Reply` - 답글
        * `Mungple` - 새로운 멍플
        * `Birthday` - 생일
        """)
public enum NotifyType {
    Comment("1",
            "https://www.reward.delgo.pet/cert/",
            "댓글을 받았어요",
            " 님이 댓글을 남겼어요"),

    Reaction("1",
            "https://www.reward.delgo.pet/cert/",
            " 보호자님 🐕",
            "작성한 게시물에 친구 강아지가 반응했어요!"),

    Reply("3",
            "https://www.reward.delgo.pet/cert/",
            " 보호자님 🐕",
            "작성한 댓글에 새로운 답글이 달렸어요!"),
    Mungple("4",
            "https://www.reward.delgo.pet/detail/",
            "새로운 동반 장소\uD83D\uDCCD",
            "새로운 멍플이 추가 되었어요. Delgo 앱에서 확인해봐요."),
    Birthday("5",
            "",
            " 보호자님 🐕",
            "생일을 축하해요! 오늘 추억을 기록하는건 어떨까요?");

    private final String tag; //
    private final String url;
    private final String title;
    private final String body;

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static NotifyType from(String s) {
        return NotifyType.valueOf(s);
    }
}