package com.delgo.reward.user.service;

import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserQueryServiceTest {
    @Autowired
    UserQueryService userQueryService;

    @Test
    void getOneByEmail() {
        // given
        String email = "cjsrnr1114@naver.com";

        // when
        User user = userQueryService.getOneByEmail(email);

        // then
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    void getOneByEmail_예외처리() {
        // given
        String email = "exception";

        // when

        // then
        assertThatThrownBy(() -> {
            userQueryService.getOneByEmail(email);
        }).isInstanceOf(NotFoundDataException.class);
    }

    @Test
    void getOneByUserId() {
        // given
        int userId = 1;

        // when
        User user = userQueryService.getOneByUserId(1);

        // then
        assertThat(user.getUserId()).isEqualTo(userId);
    }

    @Test
    void getOneByUserId_예외처리() {
        // given
        int userId = -1;

        // when

        // then
        assertThatThrownBy(() -> {
            userQueryService.getOneByUserId(userId);
        }).isInstanceOf(NotFoundDataException.class);
    }

    @Test
    void getOneByPhoneNo() {
        // given
        String phoneNo = "01062511583";

        // when
        User user = userQueryService.getOneByPhoneNo(phoneNo);

        // then
        assertThat(user.getPhoneNo()).isEqualTo(phoneNo);
    }

    @Test
    void getOneByPhoneNo_예외처리() {
        // given
        String phoneNo = "exception";

        // when

        // then
        assertThatThrownBy(() -> {
            userQueryService.getOneByPhoneNo(phoneNo);
        }).isInstanceOf(NotFoundDataException.class);
    }

    @Test
    void getOneByAppleUniqueNo() {
        // given
        String appleUniqueNo = "testAppleUniqueNo";

        // when
        User user = userQueryService.getOneByAppleUniqueNo(appleUniqueNo);

        // then
        assertThat(user.getAppleUniqueNo()).isEqualTo(appleUniqueNo);
    }

    @Test
    void getOneByAppleUniqueNo_예외처리() {
        // given
        String appleUniqueNo = "exception";

        // when

        // then
        assertThatThrownBy(() -> {
            userQueryService.getOneByAppleUniqueNo(appleUniqueNo);
        }).isInstanceOf(NotFoundDataException.class);
    }

    @Test
    void getPagingListBySearchWord() {
        // given
        String searchWord = "동재";
        Pageable pageable = PageRequest.of(0,2);

        // when
        Page<User> userPage = userQueryService.getPagingListBySearchWord(searchWord, pageable);

        // then
        assertThat(userPage.getContent().size()).isEqualTo(pageable.getPageSize());
        assertThat(userPage.getContent().get(0).getName()).contains(searchWord);
        assertThat(userPage.getContent().get(1).getName()).contains(searchWord);
    }

    @Test
    void getListByPGeoCode() {
        // given
        String pGeoCode = "101000";

        // when
        List<User> userList = userQueryService.getListByPGeoCode(pGeoCode);

        // then
        assertThat(userList).extracting(User::getPGeoCode).containsOnly(pGeoCode);
    }

    @Test
    void isPhoneNoExisting() {
        // given
        String phoneNo = "01062511583";

        // when
        boolean result = userQueryService.isPhoneNoExisting(phoneNo);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void isEmailExisting() {
        // given
        String email = "cjsrnr1114@naver.com";

        // when
        boolean result = userQueryService.isEmailExisting(email);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void isNameExisting() {
        // given
        String name = "이동재";

        // when
        boolean result = userQueryService.isNameExisting(name);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void checkNotificationPermission() {
        // given
        int userId = 364;

        // when
        boolean result = userQueryService.checkNotificationPermission(userId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void isAppleUniqueNoExisting() {
        // given
        String appleUniqueNo = "testAppleUniqueNo";

        // when
        boolean result = userQueryService.isAppleUniqueNoExisting(appleUniqueNo);

        // then
        assertThat(result).isTrue();
    }
}