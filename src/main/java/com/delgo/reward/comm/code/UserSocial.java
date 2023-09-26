package com.delgo.reward.comm.code;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(enumAsRef = true, description = """
        소셜 로그인 종류:
        * `D` - Delgo
        * `K` - Kakao
        * `N` - Naver
        * `A` - Apple
        """)
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
