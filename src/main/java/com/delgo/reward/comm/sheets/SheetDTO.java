package com.delgo.reward.comm.sheets;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.mongoDomain.MongoMungple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SheetDTO {
    private String isRegist; // 등록 여부
    private String createDate; // 등록날짜
    private String placeName; // 업체명(상호명)
    private String placeNameEn; // 업체명(상호명)
    private String address; // 업체 주소(지번)
    private String phoneNo; // 연락처
    private String businessHour; // 영업시간
    private String residentDogName; // 상주견
    private String instaId; // 인스타그램
    private String acceptSize; // 강아지 동반 안내
    private String enterDesc; // 강아지 동반 안내 매장 설명문
    private String parkingInfo; // 주차 정보
    private String isParking; // 주차 공간
    private String representMenuTitle; // 강아지대표매뉴

    public SheetDTO(List<Object> row) {
        this.isRegist = !row.isEmpty() && row.get(0) != null ? (String) row.get(0) : "";
        this.createDate = row.size() > 1 && row.get(1) != null ? (String) row.get(1) : "";
        this.placeName = row.size() > 2 && row.get(2) != null ? (String) row.get(2) : "";
        this.placeNameEn = row.size() > 3 && row.get(3) != null ? (String) row.get(3) : "";
        this.address = row.size() > 4 && row.get(4) != null ? (String) row.get(4) : "";
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


    public MongoMungple toMongoEntity(CategoryCode categoryCode, Location location){
        return MongoMungple.builder()
                .categoryCode(categoryCode.getCode())
                .phoneNo(phoneNo)
                .placeName(placeName)
                .placeNameEn(placeNameEn)
                .roadAddress(location.getRoadAddress())
                .jibunAddress(location.getJibunAddress())
                .geoCode(location.getGeoCode())
                .pGeoCode(location.getPGeoCode())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .location(new GeoJsonPoint(
                        Double.parseDouble(location.getLongitude()),
                        Double.parseDouble(location.getLatitude())))
                .instaId(instaId)
                .isParking(Boolean.valueOf(isParking))
                .parkingInfo(parkingInfo)
                .residentDogName(residentDogName)
                .representMenuTitle(representMenuTitle)
                .enterDesc(enterDesc)
                .build();
    }
}
