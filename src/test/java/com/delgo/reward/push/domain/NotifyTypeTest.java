package com.delgo.reward.push.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class NotifyTypeTest {

    @Test
    void bodyByComment() {
        // given
        String senderName = "test sender name";

        // when
        String body = NotifyType.bodyByComment(List.of(senderName));

        // then
        String expectedBody = "[" + senderName + "]" + " 님이 댓글을 남겼어요";
        assertThat(body).isEqualTo(expectedBody);
    }

    @Test
    void bodyByHelper() {
        // given
        String senderName = "test sender name";

        // when
        String body = NotifyType.bodyByHelper(List.of(senderName));

        // then
        String expectedBody = "[" + senderName + "] " + "님이 도움돼요😃를 남겼어요";
        assertThat(body).isEqualTo(expectedBody);
    }

    @Test
    void bodyByCute() {
        // given
        String senderName = "test sender name";

        // when
        String body = NotifyType.bodyByCute(List.of(senderName));

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
        String body = NotifyType.bodyByMungple(List.of(address, placeName, petName));

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
        String body = NotifyType.bodyByFoundMungpleByMe(List.of(address, placeName, petName));

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
        String body = NotifyType.bodyByFoundMungpleByOther(List.of(address, placeName, petName));

        // then
        String expectedBody = "[" + petName + "]가 다녀 온 " + address + " '" + placeName + "'가 새 장소로 추가되었어요";
        assertThat(body).isEqualTo(expectedBody);
    }

    @Test
    void getTitle() {
        // given
        NotifyType notifyType = NotifyType.Comment;

        // when
        String title = notifyType.getTitle();

        // then
        String expected = "댓글을 받았어요";
        assertThat(title).isEqualTo(expected);
    }

    @Test
    void getUrl() {
        // given
        NotifyType notifyType = NotifyType.Comment;

        // when
        String url = notifyType.getUrl();

        // then
        String expected = "https://www.reward.delgo.pet/cert/";
        assertThat(url).isEqualTo(expected);
    }

    @Test
    void getBody() {
        // given
        NotifyType notifyType = NotifyType.Comment;
        List<String> input = List.of("홍길동");

        // when
        Function<List<String>, String> bodyFunction = notifyType.getBody();
        String result = bodyFunction.apply(input);

        // then
        assertThat(result).isEqualTo("[홍길동] 님이 댓글을 남겼어요");
    }
}