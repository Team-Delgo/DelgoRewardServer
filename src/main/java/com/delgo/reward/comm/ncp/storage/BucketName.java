package com.delgo.reward.comm.ncp.storage;


import lombok.Getter;

@Getter
public enum BucketName {
    CERTIFICATION(
            "reward-certification",
            "test-certification",
            "https://kr.object.ncloudstorage.com/reward-certification/",
            "https://kr.object.ncloudstorage.com/test-certification/",
            "cert"), // 산책
    MUNGPLE(
            "reward-mungple",
            "reward-mungple",
            "https://kr.object.ncloudstorage.com/reward-mungple/",
            "https://kr.object.ncloudstorage.com/reward-mungple/",
            "mungple"), // 카페
    MUNGPLE_NOTE(
            "reward-mungplenote",
            "reward-mungplenote",
            "https://kr.object.ncloudstorage.com/reward-mungplenote/",
            "https://kr.object.ncloudstorage.com/reward-mungplenote/",
            "mungplenote"),
    PROFILE(
            "reward-profile",
            "test-profile",
            "https://kr.object.ncloudstorage.com/reward-profile/",
            "https://kr.object.ncloudstorage.com/test-profile/",
            "profile"),
    DETAIL_PRICE_TAG(
            "reward-detail-price-tag",
            "reward-detail-price-tag",
            "https://kr.object.ncloudstorage.com/reward-detail-price-tag/",
            "https://kr.object.ncloudstorage.com/reward-detail-price-tag/",
            "price"),
    DETAIL_MENU(
            "reward-detail-menu",
            "reward-detail-menu",
            "https://kr.object.ncloudstorage.com/reward-detail-menu/",
            "https://kr.object.ncloudstorage.com/reward-detail-menu/",
            "menu"),
    DETAIL_MENU_BOARD(
            "reward-detail-menu-board",
            "reward-detail-menu-board",
            "https://kr.object.ncloudstorage.com/reward-detail-menu-board/",
            "https://kr.object.ncloudstorage.com/reward-detail-menu-board/",
            "menu_board"),
    DETAIL_THUMBNAIL(
            "reward-detail-thumbnail",
            "reward-detail-thumbnail",
            "https://kr.object.ncloudstorage.com/reward-detail-thumbnail/",
            "https://kr.object.ncloudstorage.com/reward-detail-thumbnail/",
            ""), // figma에서 thumbnail은 ""이다.
    DETAIL_DOG(
            "reward-detail-resident-dog",
            "reward-detail-resident-dog",
            "https://kr.object.ncloudstorage.com/reward-detail-resident-dog/",
            "https://kr.object.ncloudstorage.com/reward-detail-resident-dog/",
            "dog");

    private final String name;
    private final String testName;
    private final String url;
    private final String testUrl;
    private final String figma;

    BucketName(String name, String testName, String url, String testUrl, String figma) {
        this.name = name;
        this.testName = testName;
        this.url = url;
        this.testUrl = testUrl;
        this.figma = figma;
    }

    public static BucketName fromFigma(String figmaValue) {
        for (BucketName bucket : values()) {
            if (bucket.figma.equals(figmaValue)) {
                return bucket;
            }
        }
        throw new IllegalArgumentException("No BucketName for figma value: " + figmaValue);
    }
}