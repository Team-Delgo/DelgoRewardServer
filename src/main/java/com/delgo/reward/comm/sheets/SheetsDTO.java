package com.delgo.reward.comm.sheets;

import com.delgo.reward.mongoDomain.MongoMungple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SheetsDTO {
    private String isRegist; // 등록 여부
    private String createDate; // 등록날짜
    private String placeName; // 업체명(상호명)
    private String placeNameEn; // 업체명(상호명)
    private String jibunAddress; // 업체 주소(지번)
    private String phoneNo; // 연락처
    @ToString.Exclude
    private String businessHour; // 영업시간
    private String residentDogName; // 상주견
    private String instaId; // 인스타그램
    @ToString.Exclude
    private String acceptSize; // 강아지 동반 안내
    @ToString.Exclude
    private String enterDesc; // 강아지 동반 안내 매장 설명문
    private String parkingInfo; // 주차 정보
    private String isParking; // 주차 공간
    private String representMenuTitle; // 강아지대표매뉴

    public SheetsDTO(List<Object> row) {
        this.isRegist = !row.isEmpty() && row.get(0) != null ? (String) row.get(0) : "";
        this.createDate = row.size() > 1 && row.get(1) != null ? (String) row.get(1) : "";
        this.placeName = row.size() > 2 && row.get(2) != null ? (String) row.get(2) : "";
        this.placeNameEn = row.size() > 3 && row.get(3) != null ? (String) row.get(3) : "";
        this.jibunAddress = row.size() > 4 && row.get(4) != null ? (String) row.get(4) : "";
        this.phoneNo = row.size() > 5 && row.get(5) != null ? (String) row.get(5) : "";
        this.businessHour = row.size() > 6 && row.get(6) != null ? (String) row.get(6) : "";
        this.residentDogName = row.size() > 7 && row.get(7) != null ? (String) row.get(7) : "";
        this.instaId = row.size() > 8 && row.get(8) != null ? (String) row.get(8) : "";
        this.acceptSize = row.size() > 9 && row.get(9) != null ? (String) row.get(9) : "";
        this.enterDesc = row.size() > 10 && row.get(10) != null ? (String) row.get(10) : "";
        this.parkingInfo = row.size() > 11 && row.get(11) != null ? (String) row.get(11) : "";
        this.isParking = row.size() > 12 && row.get(12) != null ? (String) row.get(12) : "";
        this.representMenuTitle = row.size() > 13 && row.get(13) != null ? (String) row.get(13) : "";
    }
}
