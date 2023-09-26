package com.delgo.reward.comm.code;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(enumAsRef = true, description =
        "소셜 로그인 종류: \n" +
                "* `D` - Delgo\n" +
                "* `K` - Kakao\n" +
                "* `N` - Naver\n" +
                "* `A` - Apple\n")
public enum UserSocial {
    D("Delgo"),
    K("Kakao"),
    N("Naver"),
    A("Apple");

    private final String name;

    UserSocial(String name) {
        this.name = name;
    }

}
