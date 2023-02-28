package com.delgo.reward.comm.ncp.storage;


public enum BucketName {
    ACHIEVEMENTS("reward-achievements","업적 사진"),
    CERTIFICATION("reward-certification","인증 사진"), // 산책
    MUNGPLE("reward-mungple","멍플 사진"), // 카페
    MUNGPLE_NOTE("reward-mungplenote","멍플 노트"), // 식당
    PROFILE("reward-profile","유저 프로필");

    private final String name;
    private final String des;

    BucketName(String name, String des) {
        this.name = name;
        this.des = des;
    }

    public String getName() {
        return this.name;
    }
}