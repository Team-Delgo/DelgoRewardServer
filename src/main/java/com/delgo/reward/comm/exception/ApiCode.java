package com.delgo.reward.comm.exception;

public enum ApiCode {

    // -------------------------COMMON-------------------------
    SUCCESS(API.CODE.SUCCESS, "SUCCESS"), //200

    PARAM_ERROR(API.CODE.PARAM_ERROR, "PARAM_ERROR"),
    PARAM_DATE_ERROR(API.CODE.PARAM_DATE_ERROR, "PARAM_DATE_ERROR"),
    INVALID_USER_ERROR(API.CODE.INVALID_USER_ERROR, "INVALID_USER_ERROR"),

    // TOKEN ERROR
    TOKEN_ERROR(API.CODE.TOKEN_ERROR, "TOKEN_ERROR"),

    // LOGIN ERROR
    LOGIN_ERROR(API.CODE.LOGIN_ERROR, "LOGIN_ERROR"),

    // DB ERROR
    DB_FAIL(API.CODE.DB_ERROR, "DB ERROR"),
    DB_DELETE_ERROR(API.CODE.DB_DELETE_ERROR, "DB DELETE ERROR"),
    NOT_FOUND_DATA(API.CODE.NOT_FOUND_DATA, "DB NOT_FOUND_DATA"),

    // NCP ERROR
    PHOTO_UPLOAD_ERROR(API.CODE.PHOTO_UPLOAD_ERROR, "NCP PHOTO_UPLOAD_ERROR"),
    SMS_ERROR(API.CODE.SMS_ERROR, "NCP SMS ERROR"),

    // PHOTO ERROR
    PHOTO_EXTENSION_ERROR(API.CODE.PHOTO_EXTENSION_ERROR, "올바른 확장자가 아닙니다."),

    // ------------------------FUNCTION----------------------------

    // MUNGPLE ERROR
    MUNGPLE_DUPLICATE_ERROR(API.CODE.MUNGPLE_DUPLICATE_ERROR, "이미 등록된 멍플입니다."),

    // Certification ERROR
    NOT_FONUD_GPS_DATA(316,"GPS를 확인해주세요."),
    TOO_FAR_DISTANCE(API.CODE.TOO_FAR_DISTANCE, "인증 가능한 장소에 있지 않습니다."),
    CERTIFICATION_TIME_ERROR(API.CODE.CERTIFICATION_TIME_ERROR, "6시간 이내 같은 장소 인증 불가능"),
    CERTIFICATION_CATEGPRY_COUNT_ERROR(API.CODE.CERTIFICATION_CATEGPRY_COUNT_ERROR, "일별 카테고리 5번 이상 인증 불가능"),

    // PHONE NO ERROR
    PHONE_NO_NOT_EXIST(API.CODE.PHONE_NO_NOT_EXIST, "존재하지 않는 전화번호입니다."),
    PHONE_NO_DUPLICATE_ERROR(API.CODE.PHONE_NO_DUPLICATE_ERROR, "이미 존재하는 전화번호입니다."),
    EMAIL_DUPLICATE_ERROR(API.CODE.PHONE_NO_DUPLICATE_ERROR, "이미 존재하는 이메일입니다."),

    // SMS AUTH ERROR
    AUTH_DO_NOT_MATCHING(API.CODE.AUTH_DO_NOT_MATCHING, "인증에 실패하였습니다."),

    //OAuth ERROR
    OAUTH_PHONE_NO_NOT_EXIST(API.CODE.OAUTH_PHONE_NO_NOT_EXIST, "전화번호가 없습니다."),
    ANOTHER_OAUTH_CONNECT(API.CODE.ANOTHER_OAUTH_CONNECT, "이미 다른 OAuth와 연동 되어 있습니다."),
    APPLE_UNIQUE_NO_NOT_FOUND(API.CODE.APPLE_UNIQUE_NO_NOT_FOUND, "Apple Unique No를 찾지 못했습니다."),

    OAUTH_UNKNOWN_ERROR(API.CODE.OAUTH_UNKNOWN_ERROR, "OAUTH 알수 없는 오류"),
    UNKNOWN_ERROR(API.CODE.UNKNOWN_ERROR, "알수 없는 오류");

    private int code;
    private String msg;

    ApiCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static ApiCode CODE(int intCode) {
        ApiCode apiCode = null;
        for (ApiCode code : ApiCode.values()) {
            if (intCode == code.getCode()) {
                apiCode = code;
                break;
            }
        }

        if (apiCode == null) {
            apiCode = ApiCode.SUCCESS;
        }

        return apiCode;
    }

    public class API {
        public class CODE {
            // -------------------------COMMON-------------------------
            public static final int SUCCESS = 200;

            public static final int PARAM_ERROR = 301;
            public static final int PARAM_DATE_ERROR = 302;

            // TOKEN ERROR
            public static final int TOKEN_ERROR = 303;

            // LOGIN ERROR
            public static final int LOGIN_ERROR = 304;

            // DB ERROR
            public static final int DB_ERROR = 305;
            public static final int DB_DELETE_ERROR = 306;
            public static final int NOT_FOUND_DATA = 307;

            // NCP ERROR
            public static final int PHOTO_UPLOAD_ERROR = 308;
            public static final int SMS_ERROR = 309;

            // PHOTO ERROR
            public static final int PHOTO_EXTENSION_ERROR = 310;

            // INVALID_USER_ERROR
            public static final int INVALID_USER_ERROR = 315;

            // ------------------------FUNCTION----------------------------

            // MUNGPLE ERROR
            public static final int MUNGPLE_DUPLICATE_ERROR = 311;

            // Certification ERROR
            public static final int TOO_FAR_DISTANCE = 312;
            public static final int CERTIFICATION_TIME_ERROR = 313;
            public static final int CERTIFICATION_CATEGPRY_COUNT_ERROR = 314;

            //PHONE NO ERROR
            public static final int PHONE_NO_NOT_EXIST = 370;
            public static final int PHONE_NO_DUPLICATE_ERROR = 371;

            //SMS AUTH ERROR
            public static final int AUTH_DO_NOT_MATCHING = 317;

            //OAuth ERROR
            public static final int OAUTH_PHONE_NO_NOT_EXIST = 380;
            public static final int ANOTHER_OAUTH_CONNECT = 381;
            public static final int APPLE_UNIQUE_NO_NOT_FOUND = 383;

            public static final int UNKNOWN_ERROR = 1000;
            public static final int OAUTH_UNKNOWN_ERROR = 1001;
        }
    }
}