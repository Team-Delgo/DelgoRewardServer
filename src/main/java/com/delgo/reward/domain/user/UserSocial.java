package com.delgo.reward.domain.user;

public enum UserSocial {
    D("Delgo"),
    K("Kakao"),
    N("Naver"),
    A("Apple");

    private final String name;

    UserSocial(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

}
