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
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
public class SheetDTO {
    private Boolean isRegist; // 등록 여부
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
    private Boolean isParking; // 주차 공간
    private String representMenuTitle; // 강아지대표매뉴

    public SheetDTO(List<Object> row) {
        this.isRegist = getValue(row, 0).map(Boolean::parseBoolean).orElse(false);
        this.createDate = getValue(row, 1).orElse("");
        this.placeName = getValue(row, 2).orElse("");
        this.placeNameEn = getValue(row, 3).orElse("");
        this.address = getValue(row, 4).orElse("");
        this.phoneNo = getValue(row, 5).orElse("");
        this.businessHour = getValue(row, 6).orElse("");
        this.residentDogName = getValue(row, 7).orElse("");
        this.instaId = getValue(row, 8).orElse("");
        this.acceptSize = getValue(row, 9).orElse("");
        this.enterDesc = getValue(row, 10).orElse("");
        this.parkingInfo = getValue(row, 11).orElse("");
        this.isParking = getValue(row, 12).map(Boolean::parseBoolean).orElse(false);
        this.representMenuTitle = getValue(row, 13).orElse("");
    }

    private Optional<String> getValue(List<Object> row, int index) {
        if (index < row.size() && row.get(index) != null) {
            return Optional.of((String) row.get(index));
        }
        return Optional.empty();
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
                .isParking(isParking)
                .parkingInfo(parkingInfo)
                .residentDogName(residentDogName)
                .representMenuTitle(representMenuTitle)
                .enterDesc(enterDesc)
                .build();
    }
}
