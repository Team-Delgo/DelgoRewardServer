package com.delgo.reward.comm.code;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BusinessHourCode {

    MON("MON", "월요일","휴무"),
    TUE("TUE", "화요일","휴무"),
    WED("WED", "수요일","휴무"),
    THU("THU", "목요일","휴무"),
    FRI("FRI", "금요일","휴무"),
    SAT("SAT", "토요일","휴무"),
    SUN("SUN", "일요일","휴무"),
    HOLIDAY("HOLIDAY", "휴일",""),
    BREAK_TIME("HOL", "쉬는 시간",""),
    LAST_ORDER("LAST_ORDER", "마지막 주문 시간","");

    private final String code;
    private final String desc;
    private final String defaultValue;

    BusinessHourCode(String code, String desc, String defaultValue) {
        this.code = code;
        this.desc = desc;
        this.defaultValue = defaultValue;
    }

    public String getCode(){
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static BusinessHourCode from(String s) {
        return BusinessHourCode.valueOf(s);
    }
}