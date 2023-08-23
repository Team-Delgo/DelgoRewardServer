package com.delgo.reward.comm.code;

public enum APICode {
    // -------------------------COMMON-------------------------
    SUCCESS(CODE.SUCCESS, MSG.SUCCESS),

    PARAM_ERROR(CODE.PARAM_ERROR, MSG.PARAM_ERROR),
    PARAM_DATE_ERROR(CODE.PARAM_DATE_ERROR, MSG.PARAM_DATE_ERROR),
    INVALID_USER_ERROR(CODE.INVALID_USER_ERROR, MSG.INVALID_USER_ERROR),

    // TOKEN ERROR
    TOKEN_ERROR(CODE.TOKEN_ERROR, MSG.TOKEN_ERROR),
    REFRESH_TOKEN_ERROR(CODE.TOKEN_ERROR, MSG.TOKEN_ERROR),

    // LOGIN ERROR
    LOGIN_ERROR(CODE.LOGIN_ERROR, MSG.LOGIN_ERROR),

    // DB ERROR
    DB_FAIL(CODE.DB_ERROR, MSG.DB_FAIL),
    DB_DELETE_ERROR(CODE.DB_DELETE_ERROR, MSG.DB_DELETE_ERROR),
    NOT_FOUND_DATA(CODE.NOT_FOUND_DATA, MSG.NOT_FOUND_DATA),

    // NCP ERROR
    PHOTO_UPLOAD_ERROR(CODE.PHOTO_UPLOAD_ERROR, MSG.PHOTO_UPLOAD_ERROR),
    SMS_ERROR(CODE.SMS_ERROR, MSG.SMS_ERROR),

    // PHOTO ERROR
    PHOTO_EXTENSION_ERROR(CODE.PHOTO_EXTENSION_ERROR, MSG.PHOTO_EXTENSION_ERROR),

    // ------------------------FUNCTION----------------------------

    // MUNGPLE ERROR
    MUNGPLE_DUPLICATE_ERROR(CODE.MUNGPLE_DUPLICATE_ERROR, MSG.MUNGPLE_DUPLICATE_ERROR),

    // Certification ERROR
    TOO_FAR_DISTANCE(CODE.TOO_FAR_DISTANCE, MSG.TOO_FAR_DISTANCE),
    CERTIFICATION_TIME_ERROR(CODE.CERTIFICATION_TIME_ERROR, MSG.CERTIFICATION_TIME_ERROR),
    CERTIFICATION_CATEGPRY_COUNT_ERROR(CODE.CERTIFICATION_CATEGPRY_COUNT_ERROR, MSG.CERTIFICATION_CATEGPRY_COUNT_ERROR),

    // PHONE NO ERROR
    PHONE_NO_NOT_EXIST(CODE.PHONE_NO_NOT_EXIST, MSG.PHONE_NO_NOT_EXIST),
    PHONE_NO_DUPLICATE_ERROR(CODE.PHONE_NO_DUPLICATE_ERROR, MSG.PHONE_NO_DUPLICATE_ERROR),
    EMAIL_DUPLICATE_ERROR(CODE.PHONE_NO_DUPLICATE_ERROR, MSG.EMAIL_DUPLICATE_ERROR),

    // SMS AUTH ERROR
    AUTH_DO_NOT_MATCHING(CODE.AUTH_DO_NOT_MATCHING, MSG.AUTH_DO_NOT_MATCHING),

    //OAuth ERROR
    OAUTH_PHONE_NO_NOT_EXIST(CODE.OAUTH_PHONE_NO_NOT_EXIST, MSG.OAUTH_PHONE_NO_NOT_EXIST),
    ANOTHER_OAUTH_CONNECT(CODE.ANOTHER_OAUTH_CONNECT, MSG.ANOTHER_OAUTH_CONNECT),
    APPLE_UNIQUE_NO_NOT_FOUND(CODE.APPLE_UNIQUE_NO_NOT_FOUND, MSG.APPLE_UNIQUE_NO_NOT_FOUND),

    OAUTH_SERVER_ERROR(CODE.OAUTH_SERVER_ERROR, MSG.OAUTH_SERVER_ERROR),
    SERVER_ERROR(CODE.SERVER_ERROR, MSG.SERVER_ERROR);

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


    public static class CODE {
        // -------------------------COMMON-------------------------
        public static final String SUCCESS = "200";

        public static final String PARAM_ERROR = "301";
        public static final String PARAM_DATE_ERROR = "302";

        // TOKEN ERROR
        public static final String TOKEN_ERROR = "303";

        // LOGIN ERROR
        public static final String LOGIN_ERROR = "304";

        // DB ERROR
        public static final String DB_ERROR = "305";
        public static final String DB_DELETE_ERROR = "306";
        public static final String NOT_FOUND_DATA = "307";

        // NCP ERROR
        public static final String PHOTO_UPLOAD_ERROR = "308";
        public static final String SMS_ERROR = "309";

        // PHOTO ERROR
        public static final String PHOTO_EXTENSION_ERROR = "310";

        // INVALID_USER_ERROR
        public static final String INVALID_USER_ERROR = "315";

        // ------------------------FUNCTION----------------------------

        // MUNGPLE ERROR
        public static final String MUNGPLE_DUPLICATE_ERROR = "311";

        // Certification ERROR
        public static final String TOO_FAR_DISTANCE = "312";
        public static final String CERTIFICATION_TIME_ERROR = "313";
        public static final String CERTIFICATION_CATEGPRY_COUNT_ERROR = "314";

        //PHONE NO ERROR
        public static final String PHONE_NO_NOT_EXIST = "370";
        public static final String PHONE_NO_DUPLICATE_ERROR = "371";

        //SMS AUTH ERROR
        public static final String AUTH_DO_NOT_MATCHING = "317";

        //OAuth ERROR
        public static final String OAUTH_PHONE_NO_NOT_EXIST = "380";
        public static final String ANOTHER_OAUTH_CONNECT = "381";
        public static final String APPLE_UNIQUE_NO_NOT_FOUND = "383";

        public static final String SERVER_ERROR = "1000";
        public static final String OAUTH_SERVER_ERROR = "1001";
    }

    public static class MSG {
        // -------------------------COMMON-------------------------
        public static final String SUCCESS = "SUCCESS";

        public static final String PARAM_ERROR = "PARAM_ERROR";
        public static final String PARAM_DATE_ERROR = "PARAM_DATE_ERROR";
        public static final String INVALID_USER_ERROR = "INVALID_USER_ERROR";

        // TOKEN ERROR
        public static final String TOKEN_ERROR = "TOKEN_ERROR";
        public static final String REFRESH_TOKEN_ERROR = "REFRESH_TOKEN_ERROR";

        // LOGIN ERROR
        public static final String LOGIN_ERROR = "LOGIN_ERROR";

        // DB ERROR
        public static final String DB_FAIL = "DB FAIL";
        public static final String DB_DELETE_ERROR = "DB DELETE ERROR";
        public static final String NOT_FOUND_DATA = "DB NOT_FOUND_DATA";

        // NCP ERROR
        public static final String PHOTO_UPLOAD_ERROR = "NCP PHOTO_UPLOAD_ERROR";
        public static final String SMS_ERROR = "NCP SMS ERROR";

        // PHOTO ERROR
        public static final String PHOTO_EXTENSION_ERROR = "올바른 확장자가 아닙니다.";

        // ------------------------FUNCTION----------------------------

        // MUNGPLE ERROR
        public static final String MUNGPLE_DUPLICATE_ERROR = "이미 등록된 멍플입니다.";

        // Certification ERROR
        public static final String TOO_FAR_DISTANCE = "인증 가능한 장소에 있지 않습니다.";
        public static final String CERTIFICATION_TIME_ERROR = "6시간 이내 같은 장소 인증 불가능";
        public static final String CERTIFICATION_CATEGPRY_COUNT_ERROR = "일별 카테고리 5번 이상 인증 불가능";

        // PHONE NO ERROR
        public static final String PHONE_NO_NOT_EXIST = "존재하지 않는 전화번호입니다.";
        public static final String PHONE_NO_DUPLICATE_ERROR = "이미 존재하는 전화번호입니다.";
        public static final String EMAIL_DUPLICATE_ERROR = "이미 존재하는 이메일입니다.";

        // SMS AUTH ERROR
        public static final String AUTH_DO_NOT_MATCHING = "인증에 실패하였습니다.";

        //OAuth ERROR
        public static final String OAUTH_PHONE_NO_NOT_EXIST = "전화번호가 없습니다.";
        public static final String ANOTHER_OAUTH_CONNECT = "이미 다른 OAuth와 연동 되어 있습니다.";
        public static final String APPLE_UNIQUE_NO_NOT_FOUND = "Apple Unique No를 찾지 못했습니다.";

        public static final String OAUTH_SERVER_ERROR = "OAUTH 알수 없는 오류";
        public static final String SERVER_ERROR = "알수 없는 오류";
    }

}