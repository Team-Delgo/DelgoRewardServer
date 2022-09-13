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

    // ------------------------FUNCTION----------------------------
    // COUPON ERROR
    COUPON_SELECT_ERROR(API.CODE.COUPON_SELECT_ERROR, "존재하지 않는 쿠폰 번호입니다."),
    COUPON_DUPLICATE_ERROR(API.CODE.COUPON_DUPLICATE_ERROR, "이미 존재하는 쿠폰입니다."),

    //REVIEW ERROR
    REVIEW_DUPLICATE_ERROR(API.CODE.REVIEW_DUPLICATE_ERROR, "REVIEW_DUPLICATE_ERROR"),
    REVIEW_NOT_EXIST(API.CODE.REVIEW_NOT_EXIST, "존재하지 않는 리뷰입니다."),
    REVIEW_PHOTO_COUNT_ERROR(API.CODE.REVIEW_PHOTO_COUNT_ERROR, "리뷰 사진의 수가 4개를 초과합니다."),
    PHOTO_EXTENSION_ERROR(API.CODE.PHOTO_EXTENSION_ERROR, "올바른 확장자가 아닙니다."),

    //BOOKING ERROR
    BOOKING_NOT_EXIST(API.CODE.BOOKING_NOT_EXIST, "BOOKING_NOT_EXIST"),
    ALREADY_BOOKING_PLACE(API.CODE.ALREADY_BOOKING_PLACE, "이미 예약이 존재하는 숙소입니다."),

    //CANCEL ERROR
    REFUND_ZERO(API.CODE.REFUND_ZERO, "환불금이 0원입니다."),

    // EMAIL ERROR
    EMAIL_NOT_EXIST(API.CODE.EMAIL_NOT_EXIST, "존재하지 않는 이메일입니다."),
    EMAIL_DUPLICATE_ERROR(API.CODE.EMAIL_DUPLICATE_ERROR, "이미 존재하는 이메일입니다."),

    // SMS AUTH ERROR
    AUTH_DO_NOT_MATCHING(API.CODE.AUTH_DO_NOT_MATCHING, "인증에 실패하였습니다."),

    // NAME ERROR
    NAME_DUPLICATE_ERROR(API.CODE.NAME_DUPLICATE_ERROR, "이미 존재하는 이름입니다."),

    // PHONE NO ERROR
    PHONE_NO_NOT_EXIST(API.CODE.PHONE_NO_NOT_EXIST, "존재하지 않는 전화번호입니다."),
    PHONE_NO_DUPLICATE_ERROR(API.CODE.PHONE_NO_DUPLICATE_ERROR, "이미 존재하는 전화번호입니다."),

    //OAuth ERROR
    KAKAO_PHONE_NO_NOT_EXIST(API.CODE.KAKAO_PHONE_NO_NOT_EXIST, "KAKAO에 전화번호가 없습니다."),
    KAKAO_NOT_CONNECT(API.CODE.KAKAO_NOT_CONNECT, "KAKAO와 연동이 되어 있지 않습니다.."),
    ANOTHER_OAUTH_CONNECT(API.CODE.ANOTHER_OAUTH_CONNECT, "이미 다른 OAuth와 연동 되어 있습니다."),



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