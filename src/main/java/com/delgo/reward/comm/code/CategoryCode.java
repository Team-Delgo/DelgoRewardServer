package com.delgo.reward.comm.code;

public enum CategoryCode {
    TOTAL("CA0000","전체"),
    CA0001("CA0001","산책"), // 산책
    CA0002("CA0002","카페"), // 카페
    CA0003("CA0003","식당"), // 식당
    CA0004("CA0004","목욕"), // 목욕
    CA0005("CA0005","미용"), // 미용
    CA0006("CA0006","병원"); // 병원

    private final String code;
    private final String value;

    CategoryCode(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode(){
        return this.code;
    }

    public String getValue(){
        return this.value;
    }
}