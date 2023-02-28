package com.delgo.reward.comm.ncp.storage;


public enum BucketName {
    ACHIEVEMENTS("reward-achievements","https://kr.object.ncloudstorage.com/reward-achievements/"),
    CERTIFICATION("reward-certification","https://kr.object.ncloudstorage.com/reward-certification/"), // 산책
    MUNGPLE("reward-mungple", "https://kr.object.ncloudstorage.com/reward-mungple/"), // 카페
    MUNGPLE_NOTE("reward-mungplenote", "https://kr.object.ncloudstorage.com/reward-mungplenote/"), // 식당
    PROFILE("reward-profile","https://kr.object.ncloudstorage.com/reward-profile/");

    private final String name;
    private final String url;

    BucketName(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }
}