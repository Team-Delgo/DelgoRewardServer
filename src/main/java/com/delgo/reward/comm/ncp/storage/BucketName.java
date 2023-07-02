package com.delgo.reward.comm.ncp.storage;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum BucketName {
    ACHIEVEMENTS(
            "reward-achievements",
            "reward-achievements",
            "https://kr.object.ncloudstorage.com/reward-achievements/",
            "https://kr.object.ncloudstorage.com/reward-achievements/"),
    CERTIFICATION(
            "reward-certification",
            "test-certification",
            "https://kr.object.ncloudstorage.com/reward-certification/",
            "https://kr.object.ncloudstorage.com/test-certification/"), // 산책
    MUNGPLE(
            "reward-mungple",
            "reward-mungple",
            "https://kr.object.ncloudstorage.com/reward-mungple/",
            "https://kr.object.ncloudstorage.com/reward-mungple/"), // 카페
    MUNGPLE_NOTE(
            "reward-mungplenote",
            "reward-mungplenote",
            "https://kr.object.ncloudstorage.com/reward-mungplenote/",
            "https://kr.object.ncloudstorage.com/reward-mungplenote/"), // 식당
    PROFILE(
            "reward-profile",
            "test-profile",
            "https://kr.object.ncloudstorage.com/reward-profile/",
            "https://kr.object.ncloudstorage.com/test-profile/"),
    DETAIL_PRICE_TAG(
            "reward-detail-price-tag",
            "reward-detail-price-tag",
            "https://kr.object.ncloudstorage.com/reward-detail-price-tag/",
            "https://kr.object.ncloudstorage.com/reward-detail-price_tag/"),
    DETAIL_MENU(
            "reward-detail-menu",
            "reward-detail-menu",
            "https://kr.object.ncloudstorage.com/reward-detail-menu/",
            "https://kr.object.ncloudstorage.com/reward-detail-menu/"),
    DETAIL_MENU_BOARD(
            "reward-detail-menu-board",
            "reward-detail-menu-board",
            "https://kr.object.ncloudstorage.com/reward-detail-menu-board/",
            "https://kr.object.ncloudstorage.com/reward-detail-menu-board/"),
    DETAIL_THUMBNAIL(
            "reward-detail-thumbnail",
            "reward-detail-thumbnail",
            "https://kr.object.ncloudstorage.com/reward-detail-thumbnail/",
            "https://kr.object.ncloudstorage.com/reward-detail-thumbnail/");

    private final String name;
    private final String testName;
    private final String url;
    private final String testUrl;

    BucketName(String name, String testName, String url, String testUrl) {
        this.name = name;
        this.testName = testName;
        this.url = url;
        this.testUrl = testUrl;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public String getTestName() {
        return this.testName;
    }

    public String getTestUrl() {
        return this.testUrl;
    }
}