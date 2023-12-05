package com.delgo.reward.comm.code;


public enum APICode {
    // -------------------------COMMON-------------------------
    SUCCESS("200", "SUCCESS"),
    PARAM_ERROR("301", "PARAM_ERROR"),
    INVALID_USER_ERROR("315", "INVALID_USER_ERROR"),

    // TOKEN ERROR
    TOKEN_ERROR("303", "TOKEN_ERROR"),
    TOKEN_EXPIRED("303", "TOKEN_EXPIRED"),
    TOKEN_VERIFY_ERROR("303", "TOKEN_VERIFY_ERROR"),
    DB_TOKEN_ERROR("303", "DB_TOKEN_ERROR"),

    // LOGIN ERROR
    LOGIN_ERROR("304", "LOGIN_ERROR"),

    // DB ERROR
    NOT_FOUND_DATA("307", "DB NOT_FOUND_DATA"),

    // NCP ERROR
    SMS_ERROR("309", "NCP SMS ERROR"),

    // PHOTO ERROR
    PHOTO_ERROR("308", "PHOTO_ERROR"),

    // PHONE NO ERROR
    PHONE_NO_NOT_EXIST("370", "존재하지 않는 전화번호입니다."),
    PHONE_NO_DUPLICATE_ERROR("371", "이미 존재하는 전화번호입니다."),
    EMAIL_DUPLICATE_ERROR("371", "이미 존재하는 이메일입니다."),

    // SMS AUTH ERROR
    AUTH_DO_NOT_MATCHING("317", "인증에 실패하였습니다."),

    //OAuth ERROR
    OAUTH_PHONE_NO_NOT_EXIST("380", "전화번호가 없습니다."),
    ANOTHER_OAUTH_CONNECT("381", "이미 다른 OAuth와 연동 되어 있습니다."),
    APPLE_UNIQUE_NO_NOT_FOUND("383", "Apple Unique No를 찾지 못했습니다."),

    //Figma ERROR
    FIGMA_ERROR("390", "FIGMA_ERROR"),

    OAUTH_SERVER_ERROR("1001", "OAUTH 알수 없는 오류"),
    SERVER_ERROR("1000", "알수 없는 오류");

    private String code;
    private String msg;

    APICode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return Integer.parseInt(this.code);
    }

    public String getMsg() {
        return this.msg;
    }
}
