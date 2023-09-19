package com.delgo.reward.comm.code;


public enum APICode {
    // -------------------------COMMON-------------------------
    SUCCESS("200", "SUCCESS"),
    PARAM_ERROR("301", "PARAM_ERROR"),
    PARAM_DATE_ERROR("302", "PARAM_DATE_ERROR"),
    INVALID_USER_ERROR("315", "INVALID_USER_ERROR"),

    // TOKEN ERROR
    TOKEN_ERROR("303", "TOKEN_ERROR"),
    DB_TOKEN_ERROR("303", "DB_TOKEN_ERROR"),

    // LOGIN ERROR
    LOGIN_ERROR("304", "LOGIN_ERROR"),

    // DB ERROR
    DB_FAIL("305", "DB FAIL"),
    DB_DELETE_ERROR("306", "DB DELETE ERROR"),
    NOT_FOUND_DATA("307", "DB NOT_FOUND_DATA"),

    // NCP ERROR
    PHOTO_UPLOAD_ERROR("308", "NCP PHOTO_UPLOAD_ERROR"),
    SMS_ERROR("309", "NCP SMS ERROR"),

    // PHOTO ERROR
    PHOTO_EXTENSION_ERROR("310", "올바른 확장자가 아닙니다."),
    MUNGPLE_DUPLICATE_ERROR("311", "이미 등록된 멍플입니다."),

    // Certification ERROR
    TOO_FAR_DISTANCE("312", "인증 가능한 장소에 있지 않습니다."),
    CERTIFICATION_TIME_ERROR("313", "6시간 이내 같은 장소 인증 불가능"),
    CERTIFICATION_CATEGPRY_COUNT_ERROR("314", "일별 카테고리 5번 이상 인증 불가능"),

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
