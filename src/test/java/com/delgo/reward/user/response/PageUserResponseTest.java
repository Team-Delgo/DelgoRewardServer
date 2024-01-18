package com.delgo.reward.user.response;

import com.delgo.reward.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageUserResponseTest {

    @Test
    void from() {
        // given
        UserResponse userResponse1 = UserResponse.builder().userId(1).build();
        UserResponse userResponse2 = UserResponse.builder().userId(2).build();
        List<UserResponse> userResponseList = List.of(userResponse1, userResponse2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(new ArrayList<>(), pageRequest, userResponseList.size());

        // when
        PageUserResponse pageUserResponse = PageUserResponse.from(userPage, userResponseList);

        // then
        assertThat(pageUserResponse.getSize()).isEqualTo(pageRequest.getPageSize());
        assertThat(pageUserResponse.getNumber()).isEqualTo(pageRequest.getPageNumber());
        assertThat(pageUserResponse.isLast()).isEqualTo(userPage.isLast());
        assertThat(pageUserResponse.getTotalCount()).isEqualTo(userPage.getTotalElements());
        assertThat(pageUserResponse.getContent().size()).isEqualTo(userResponseList.size());
    }
}