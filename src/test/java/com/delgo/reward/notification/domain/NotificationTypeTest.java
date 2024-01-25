package com.delgo.reward.notification.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class NotificationTypeTest {

    @Test
    void bodyByComment() {
        // given
        String senderName = "test sender name";

        // when
        String body = NotificationType.bodyByComment(List.of(senderName));

        // then
        String expectedBody = "[" + senderName + "]" + " 님이 댓글을 남겼어요";
        assertThat(body).isEqualTo(expectedBody);
    }

    @Test
    void bodyByHelper() {
        // given
        String senderName = "test sender name";

        // when
        String body = NotificationType.bodyByHelper(List.of(senderName));

        // then
        String expectedBody = "[" + senderName + "] " + "님이 도움돼요😃를 남겼어요";
        assertThat(body).isEqualTo(expectedBody);
    }

    @Test
    void bodyByCute() {
        // given
        String senderName = "test sender name";

        // when
        String body = NotificationType.bodyByCute(List.of(senderName));

        // then
        String expectedBody = "[" + senderName + "] " + "님이 귀여워요😍를 남겼어요";
        assertThat(body).isEqualTo(expectedBody);
    }

    @Test
    void bodyByMungple() {
        // given
        String address = "test address";
        String placeName = "test place name";
        String petName = "test pet Name";

        // when
        String body = NotificationType.bodyByMungple(List.of(address, placeName, petName));

        // then
        String expectedBody = address + " '" + placeName + "' 도 [" + petName + "]와 함께 갈 수 있대요";
        assertThat(body).isEqualTo(expectedBody);
    }

    @Test
    void bodyByFoundMungpleByMe() {
        // given
        String address = "test address";
        String placeName = "test place name";
        String petName = "test pet Name";

        // when
        String body = NotificationType.bodyByFoundMungpleByMe(List.of(address, placeName, petName));

        // then
        String expectedBody = "[" + petName + "]와 다녀온 '" + address + " " + placeName + "'가 새 장소로 추가되었어요";
        assertThat(body).isEqualTo(expectedBody);
    }

    @Test
    void bodyByFoundMungpleByOther() {
        // given
        String address = "test address";
        String placeName = "test place name";
        String petName = "test pet Name";

        // when
        String body = NotificationType.bodyByFoundMungpleByOther(List.of(address, placeName, petName));

        // then
        String expectedBody = "[" + petName + "]가 다녀 온 " + address + " '" + placeName + "'가 새 장소로 추가되었어요";
        assertThat(body).isEqualTo(expectedBody);
    }

    @Test
    void bodyByBirthday() {
        // given
        String petName = "test pet Name";

        // when
        String body = NotificationType.bodyByBirthday(List.of(petName));

        // then
        String expectedBody = "오늘은 [" + petName + "]의 생일이에요. [" + petName + "]와 함께 즐거운 하루 보내세요❤";
        assertThat(body).isEqualTo(expectedBody);
    }

    @Test
    void getTitle() {
        // given
        NotificationType notificationType = NotificationType.Comment;

        // when
        String title = notificationType.getTitle();

        // then
        String expected = "댓글을 받았어요";
        assertThat(title).isEqualTo(expected);
    }

    @Test
    void getUrl() {
        // given
        NotificationType notificationType = NotificationType.Comment;

        // when
        String url = notificationType.getUrl();

        // then
        String expected = "https://www.reward.delgo.pet/cert/";
        assertThat(url).isEqualTo(expected);
    }

    @Test
    void getBody() {
        // given
        NotificationType notificationType = NotificationType.Comment;
        List<String> input = List.of("홍길동");

        // when
        Function<List<String>, String> bodyFunction = notificationType.getBody();
        String result = bodyFunction.apply(input);

        // then
        assertThat(result).isEqualTo("[홍길동] 님이 댓글을 남겼어요");
    }
}