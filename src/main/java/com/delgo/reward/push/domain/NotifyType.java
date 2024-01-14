package com.delgo.reward.push.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;


@Getter
@AllArgsConstructor
@Schema(enumAsRef = true, description = """
        Push 카테고리:
        * `Comment` - 댓글
        * `Helper` - 도움돼요
        * `Cute` - 귀여워요
        * `Mungple` - 델고가 등록한 멍플
        * `MungpleByMe` - 내가 등록한 멍플
        * `MungpleByOther` - 다른 사람이 등록한 멍플
        * `Birthday` - 생일
        """)
public enum NotifyType {
    Comment("댓글을 받았어요", "https://www.reward.delgo.pet/cert/", NotifyType::bodyByComment),
    Helper("도움돼요😃를 받았어요", "https://www.reward.delgo.pet/cert/", NotifyType::bodyByHelper),
    Cute("귀여워요😍를 받았어요", "https://www.reward.delgo.pet/cert/", NotifyType::bodyByCute),
    Mungple("새로운 동반 장소\uD83D\uDCCD", "https://www.reward.delgo.pet/detail/", NotifyType::bodyByMungple),
    MungpleByMe("새로운 동반 장소\uD83D\uDCCD", "https://www.reward.delgo.pet/detail/", NotifyType::bodyByFoundMungpleByMe),
    MungpleByOther("새로운 동반 장소\uD83D\uDCCD", "https://www.reward.delgo.pet/detail/", NotifyType::bodyByFoundMungpleByOther),
    Birthday("생일을 축하합니다\uD83C\uDF89", "", null);

    private final String title;
    private final String url;
    private final Function<List<String>, String> body;

    public String getBody(List<String> args) {
        return body.apply(args);
    }

    public static String bodyByComment(List<String> list) {
        String senderName = list.get(0);
        return "[" + senderName + "]" + " 님이 댓글을 남겼어요";
    }

    public static String bodyByHelper(List<String> list) {
        String senderName = list.get(0);
        return "[" + senderName + "] " + "님이 도움돼요😃를 남겼어요";
    }

    public static String bodyByCute(List<String> list) {
        String senderName = list.get(0);
        return "[" + senderName + "] " + "님이 귀여워요😍를 남겼어요";
    }

    // 송파구 ‘서은이네 카페' 도 [또리]와 함께 갈 수 있대요
    public static String bodyByMungple(List<String> list) {
        String address = list.get(0);
        String placeName = list.get(1);
        String receiverPetName = list.get(2);
        return address + " '" + placeName + "' 도 [" + receiverPetName + "]와 함께 갈 수 있대요";
    }

    // [또리]와 다녀온 [‘서은이네 카페’] 가 새 장소로 추가되었어요
    public static String bodyByFoundMungpleByMe(List<String> list) {
        String address = list.get(0);
        String placeName = list.get(1);
        String receiverPetName = list.get(2);
        return "[" + receiverPetName + "]와 다녀온 '" + address + " " + placeName + "'가 새 장소로 추가되었어요";
    }

    // [또리]가 다녀 온 송파구 [‘서은이네 카페’] 가 새 장소로 추가되었어요
    public static String bodyByFoundMungpleByOther(List<String> list) {
        String address = list.get(0);
        String placeName = list.get(1);
        String firstWriterPetName = list.get(2);
        return "[" + firstWriterPetName + "]가 다녀 온 " + address + " '" + placeName + "'가 새 장소로 추가되었어요";
    }

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static NotifyType from(String s) {
        return NotifyType.valueOf(s);
    }
}