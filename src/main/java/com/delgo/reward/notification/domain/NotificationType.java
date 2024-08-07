package com.delgo.reward.notification.domain;

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
public enum NotificationType {
    Comment("댓글을 받았어요", "https://www.reward.delgo.pet/cert/", NotificationType::bodyByComment),
    Helper("도움돼요😃를 받았어요", "https://www.reward.delgo.pet/cert/", NotificationType::bodyByHelper),
    Cute("귀여워요😍를 받았어요", "https://www.reward.delgo.pet/cert/", NotificationType::bodyByCute),
    Mungple("새로운 동반 장소\uD83D\uDCCD", "https://www.reward.delgo.pet/detail/", NotificationType::bodyByMungple),
    MungpleByMe("새로운 동반 장소\uD83D\uDCCD", "https://www.reward.delgo.pet/detail/", NotificationType::bodyByFoundMungpleByMe),
    MungpleByOther("새로운 동반 장소\uD83D\uDCCD", "https://www.reward.delgo.pet/detail/", NotificationType::bodyByFoundMungpleByOther),
    Birthday("생일을 축하합니다\uD83C\uDF89", "https://www.reward.delgo.pet", NotificationType::bodyByBirthday);

    private final String title;
    private final String url;
    private final Function<List<String>, String> body;

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
        String firstFounderPetName = list.get(2);
        return "[" + firstFounderPetName + "]가 다녀 온 " + address + " '" + placeName + "'가 새 장소로 추가되었어요";
    }

    public static String bodyByBirthday(List<String> list) {
        String petName = list.get(0);
        return "오늘은 [" + petName + "]의 생일이에요. [" + petName + "]와 함께 즐거운 하루 보내세요❤";
    }
}