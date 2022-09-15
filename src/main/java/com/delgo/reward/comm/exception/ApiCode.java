package com.delgo.reward.comm.exception;

public enum ApiCode {

    // -------------------------COMMON-------------------------
    SUCCESS(API.CODE.SUCCESS, "SUCCESS"), //200

    PARAM_ERROR(API.CODE.PARAM_ERROR, "PARAM_ERROR"),
    PARAM_DATE_ERROR(API.CODE.PARAM_DATE_ERROR, "PARAM_DATE_ERROR"),

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
    TOO_FAR_DISTANCE(API.CODE.TOO_FAR_DISTANCE, "인증 가능한 장소에 있지 않습니다."),

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
}