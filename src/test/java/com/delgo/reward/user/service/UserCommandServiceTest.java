package com.delgo.reward.user.service;

import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.user.controller.request.UserUpdate;
import com.delgo.reward.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserCommandServiceTest {
    @Autowired
    UserCommandService userCommandService;
    @Autowired
    UserQueryService userQueryService;

    @Test
    @Transactional
    void save() {
        // given
        User user = User.builder()
                .name("홍길동")
                .email("honggildong@example.com")
                .password("securePassword123!")
                .phoneNo("01062521583")
                .address("서울특별시 강남구 테헤란로 123")
                .geoCode("101180")
                .pGeoCode("101000")
                .isNotify(true)
                .version("1.0.0")
                .roles("ROLE_USER")
                .userSocial(UserSocial.D)
                .lastAccessDt(LocalDateTime.now())
                .build();

        // when
        User savedUser = userCommandService.save(user);

        // then
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @Transactional
    void updateProfile() {
        // given
        int userId = 1;
        String profileUrl = "testUrl";

        // when
        User user = userCommandService.updateProfile(userId, profileUrl);

        // then
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getProfile()).isEqualTo(profileUrl);
    }

    @Test
    @Transactional
    void updatePassword() {
        // given
        String email = "test@delgo.pet";
        String newPassword = "123456";

        // when
        User user = userCommandService.updatePassword(email, newPassword);

        // then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(newPassword);
    }

    @Test
    @Transactional
    void updateIsNotify() {
        // given
        int userId = 1;

        // when
        User user = userCommandService.updateIsNotify(userId);

        // then
        boolean expectedIsNotify = false;
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getIsNotify()).isEqualTo(expectedIsNotify);
    }

    @Test
    @Transactional
    void updateUserInfo() {
        // given
        UserUpdate userUpdate = UserUpdate.builder()
                .userId(1)
                .name("newName")
                .geoCode("101180")
                .pGeoCode("101000")
                .build();

        // when
        User user = userCommandService.updateUserInfo(userUpdate);

        // then
        assertThat(user.getUserId()).isEqualTo(userUpdate.userId());
        assertThat(user.getName()).isEqualTo(userUpdate.name());
        assertThat(user.getGeoCode()).isEqualTo(userUpdate.geoCode());
        assertThat(user.getPGeoCode()).isEqualTo(userUpdate.pGeoCode());
    }

    @Test
    @Transactional
    void deleteByUserId() {
        // given
        int userId = 1;

        // when
        userCommandService.deleteByUserId(userId);

        // then
        assertThatThrownBy(() -> {
            userQueryService.getOneByUserId(userId);
        }).isInstanceOf(NotFoundDataException.class);
    }

    @Test
    @Transactional
    void increaseViewCount() {
        // given
        int userId = 1;
        User user = userQueryService.getOneByUserId(userId);
        int viewCount = user.getViewCount();

        // when
        userCommandService.increaseViewCount(userId);

        // then
        User expected = userQueryService.getOneByUserId(userId);
        assertThat(expected.getViewCount()).isEqualTo(viewCount + 1);
    }
}