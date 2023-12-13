package com.delgo.reward.comm.googlesheet;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.FigmaException;
import com.delgo.reward.comm.ncp.geo.GeoData;
import com.delgo.reward.domain.code.Code;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.delgo.reward.comm.code.CategoryCode.MENU_CODES;

@Data
@Builder
@AllArgsConstructor
public class GoogleSheetDTO {
    public static final String ERROR_MSG = "Google Sheet에 데이터가 없습니다. ";

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
    private String figmaNodeId; // 강아지대표매뉴

    public GoogleSheetDTO(List<Object> row, CategoryCode categoryCode) {
        this.placeName = getValueOrThrow(row, 2, "placeName");
        this.placeNameEn = getValue(row, 3);
        this.address = getValueOrThrow(row, 4, "address");
        this.phoneNo = getValue(row, 5);
        this.businessHour = getValueOrThrow(row, 6, "businessHour");
        this.residentDogName = getValue(row, 7);
        this.instaId = getValue(row, 8);
        this.acceptSize = getValue(row, 9);
        this.enterDesc = getValueOrThrow(row, 10, "enterDesc");
        this.parkingInfo = getValue(row, 11);
        this.isParking = Boolean.parseBoolean(getValueOrThrow(row, 12, "isParking"));

        if(MENU_CODES.contains(categoryCode)){  // 카페, 식당
            this.representMenuTitle = getValue(row, 13);
            this.figmaNodeId = getValueOrThrow(row, 14, "figmaNodeId");
        } else {
            this.figmaNodeId = getValueOrThrow(row, 13, "figmaNodeId");
        }
    }

    public MongoMungple toMongoEntity(CategoryCode categoryCode, GeoData geoData, Code code) {
        MongoMungple mongoMungple = MongoMungple.builder()
                .categoryCode(categoryCode)
                .phoneNo(phoneNo)
                .placeName(placeName)
                .placeNameEn(placeNameEn)
                .roadAddress(geoData.getRoadAddress())
                .jibunAddress(geoData.getJibunAddress())
                .geoCode(code.getCode())
                .pGeoCode(code.getPCode())
                .latitude(geoData.getLatitude())
                .longitude(geoData.getLongitude())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .location(new GeoJsonPoint(
                        Double.parseDouble(geoData.getLongitude()),
                        Double.parseDouble(geoData.getLatitude())))
                .instaId(instaId)
                .isParking(isParking)
                .parkingInfo(parkingInfo)
                .residentDogName(residentDogName)
                .enterDesc(enterDesc)
                .build();

        if (MENU_CODES.contains(categoryCode))  // 카페, 식당
            mongoMungple.setRepresentMenuTitle(representMenuTitle);

        return mongoMungple;
    }

    private String getValueOrThrow(List<Object> row, int index, String fieldName) {
        if (index < row.size() && StringUtils.hasText((CharSequence) row.get(index))) {
            String value = (String) row.get(index);
            value = value.replace("\"", "").trim(); // 큰따옴표 제거, 앞 뒤 공백 제거

            return value;
        }
        throw new FigmaException(ERROR_MSG + "[" +this.placeName + " : " + fieldName + "]");
    }

    private String getValue(List<Object> row, int index) {
        if (index < row.size() && StringUtils.hasText((CharSequence) row.get(index))) {
            String value = (String) row.get(index);
            value = value.replace("\"", "").trim(); // 큰따옴표 제거 , 앞 뒤 공백 제거

            return value;
        }
        return "";
    }
}
