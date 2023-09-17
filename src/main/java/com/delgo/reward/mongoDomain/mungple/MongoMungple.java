package com.delgo.reward.mongoDomain.mungple;


import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.DetailCode;
import lombok.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="mungple")
public class MongoMungple {
    @Transient
    public static final String SEQUENCE_NAME = "mungple_sequence";
    // Mungple
    @Id private String id;

    @Field("mungple_id") private Integer mungpleId;
    @Field("category_code")private String categoryCode; // 카테고리 코드 ( ex. 카페, 음식점 .. )

    @Field("phone_no")private String phoneNo;
    @Field("place_name")private String placeName;
    @Field("place_name_en")private String placeNameEn;
    @Field("road_address")private String roadAddress; // 도로명 주소
    @Field("jibun_address")private String jibunAddress; // 지번 주소

    @Field("geo_code")private String geoCode; // 지역 코드
    @Field("p_geo_code")private String pGeoCode; // 부모 지역 코드
    @Field("latitude")private String latitude; // 위도
    @Field("longitude")private String longitude; // 경도

    @Field("detail_url")private String detailUrl; // 상세 페이지 url

    private boolean isActive; // 활성화 여부

    @Field("created_at") private LocalDateTime createdAt;

    // 위치 기반 조회 시 필요
    @Field("location") private GeoJsonPoint location;

    // MungpleDetail
    @Field("photo_urls")
    private List<String> photoUrls; // 매장 사진 URL List

    @Field("enter_desc")
    private String enterDesc; // 강아지 동반 안내 매장 설명문
    @Field("accept_size")
    private Map<String, DetailCode> acceptSize;  // 허용 크기 ( S, M , L )
    @Field("business_hour")
    private Map<BusinessHourCode, String> businessHour; // 운영 시간 ( 요일별로 표시 )

    @Field("insta_id")
    private String instaId; // 인스타 ID

    @Field("is_parking")
    private Boolean isParking; // 주차 가능 대수
    @Field("parking_info")
    private String parkingInfo; // 주차 정보

    // CA0002, CA0003
    @Field("resident_dog_name")
    private String residentDogName; // 상주견 이름
    @Field("resident_dog_photo")
    private String residentDogPhoto; // 상주견 사진

    @Field("represent_menu_title")
    private String representMenuTitle; // 대표 메뉴 제목
    @Field("represent_menu_photo_urls")
    private List<String> representMenuPhotoUrls; // 대표 메뉴 URL List // ※무조건 3개 이상이어야 함.

    // CA0001, CA0005, CA0006, CA0007
    @Field("is_price_tag")
    private Boolean isPriceTag; // 가격표 존재 여부
    @Field("price_tag_photo_urls")
    private List<String> priceTagPhotoUrls; // 가격표 사진

    public void setMungpleId(Integer mungpleId) {
        this.mungpleId = mungpleId;
    }

    public MongoMungple setPhoneNo(String phoneNo){
        this.phoneNo = phoneNo.replace("-","");

        return this;
    }

    public void setAcceptSize(String input) {
        acceptSize = Arrays.stream(input.replaceAll("[\n\"]", "").split(","))
                .map(s -> s.split(": "))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> DetailCode.valueOf(arr[1])
                ));
    }

    public void setBusinessHour(String input) {
        businessHour = Arrays.stream(input.replaceAll("[\n\"]", "").split(","))
                .map(s -> s.split(": "))
                .collect(Collectors.toMap(
                        arr -> BusinessHourCode.valueOf(arr[0]),
                        arr -> arr[1]));
        // Default 값 세팅
        Arrays.stream(BusinessHourCode.values())
                .forEach(code -> businessHour.computeIfAbsent(code, BusinessHourCode::getDefaultValue));
    }
}