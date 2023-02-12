package com.delgo.reward.comm.code;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PCode {
    P101000("101000", "서울시"),
    P102000("102000", "경기도"),
    P103000("103000", "광주광역시"),
    P104000("104000", "대구광역시"),
    P105000("105000", "대전광역시"),
    P106000("106000", "부산광역시"),
    P107000("107000", "울산광역시"),
    P108000("108000", "인천광역시"),
    P109000("109000", "강원도"),
    P110000("110000", "경상남도"),
    P111000("111000", "경상북도"),
    P112000("112000", "전라남도"),
    P113000("113000", "전라북도"),
    P114000("114000", "충청북도"),
    P115000("115000", "충청남도"),
    P116000("116000", "제주도");

    private final String pCode;
    private final String codeDesc;

    PCode(String pCode, String codeDesc){
        this.pCode = pCode;
        this.codeDesc = codeDesc;
    }

    public String getPCode(){
        return this.pCode;
    }
    public String getCodeDesc() {
        return this.codeDesc;
    }

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static PCode from(String s) {
        return PCode.valueOf(s);
    }
}