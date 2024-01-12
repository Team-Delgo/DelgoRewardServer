package com.delgo.reward.mungple.domain;


import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.DetailCode;
import com.delgo.reward.comm.exception.GoogleSheetException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="mungple")
public class Mungple {
    @Transient
    public static final String SEQUENCE_NAME = "mungple_sequence";
    @Setter  // Sheet Update 시 사용
    @Id private String id;

    @Setter
    @Field("mungple_id") private Integer mungpleId;
    @Field("category_code")private CategoryCode categoryCode; // 카테고리 코드 ( ex. 카페, 음식점 .. )

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

    @Setter
    @Field("represent_menu_title")
    private String representMenuTitle; // 대표 메뉴 제목
    @Field("represent_menu_photo_urls")
    private List<String> representMenuPhotoUrls = new ArrayList<>(); // 대표 메뉴 URL List // ※무조건 3개 이상이어야 함.
    @Field("represent_menu_board_photo_urls")
    private List<String> representMenuBoardPhotoUrls = new ArrayList<>(); // 대표 메뉴 URL List // ※무조건 3개 이상이어야 함.

    // CA0001, CA0005, CA0006, CA0007
    @Field("is_price_tag")
    private Boolean isPriceTag; // 가격표 존재 여부
    @Field("price_tag_photo_urls")
    private List<String> priceTagPhotoUrls = new ArrayList<>(); // 가격표 사진

    public void setAcceptSize(String input) {
        acceptSize = new HashMap<>();

        Pattern pattern = Pattern.compile("([A-Z_]+): ?([^\\n]+),?");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            try {
                String key = matcher.group(1).trim().replaceAll(",$", "");
                System.out.println("key = " + key);
                String value = matcher.group(2).trim().replaceAll(",$", "");
                System.out.println("value = " + value);
                acceptSize.put(key,DetailCode.valueOf(value));
            } catch (Exception e){
                log.error(e.getMessage());
                throw new GoogleSheetException("[강아지 동반 안내] " + matcher.group(1) + " 확인해주세요");
            }
        }
    }

    public void setBusinessHour(String input) {
        businessHour = new HashMap<>();

        Pattern pattern = Pattern.compile("([A-Z_]+): ?([^\\n]+),?");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            try {
                BusinessHourCode key = BusinessHourCode.valueOf(matcher.group(1));
                String value = matcher.group(2).trim().replaceAll(",$", ""); // 앞 뒤 공백, 마지막 콤마 제거
                businessHour.put(key, value);
            } catch (Exception e){
                log.error(e.getMessage());
                throw new GoogleSheetException("[영업 시간] " + matcher.group(1) + " 확인해주세요");
            }
        }

        // Default 값 설정
        Arrays.stream(BusinessHourCode.values())
                .forEach(code -> businessHour.computeIfAbsent(code, BusinessHourCode::getDefaultValue));
    }

    public void setFigmaPhotoData(Map<String, ArrayList<String>> listMap) {
        this.photoUrls = sortAndRetrieveUrls(listMap, "");
        this.representMenuPhotoUrls = sortAndRetrieveUrls(listMap, "menu");
        this.representMenuBoardPhotoUrls = sortAndRetrieveUrls(listMap, "menu_board");
        this.priceTagPhotoUrls = sortAndRetrieveUrls(listMap, "price");
        this.residentDogPhoto = Optional.ofNullable(listMap.get("dog"))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0))
                .orElse(null);
    }

    private List<String> sortAndRetrieveUrls(Map<String, ArrayList<String>> map, String key) {
        return Optional.ofNullable(map.get(key))
                .filter(list -> !list.isEmpty())
                .map(this::sortUrlList)
                .orElse(Collections.emptyList());
    }

    private List<String> sortUrlList(List<String> list){
        Pattern pattern = Pattern.compile("_([0-9]+)\\.webp"); // Extracts the number before ".webp"
        return list.stream()
                .sorted(Comparator.comparingInt(url -> {
                    Matcher matcher = pattern.matcher(url);
                    return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
                }))
                .collect(Collectors.toList());
    }

    public String makeBaseNameForFigma(){
        String categoryName = this.categoryCode.getName();
        String addressName = findAddressName(jibunAddress, isExistGu(jibunAddress) ? "구" : "시");

        String baseName = addressName + "_" + categoryName + "_" + placeName.replaceAll("\\s+", "");
        System.out.println("baseName = " + baseName);

        return baseName;
    }

    private boolean isExistGu(String text) {
        Pattern pattern = Pattern.compile("\\b\\S*구\\b");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            return true; // 찾은 경우 즉시 true 반환
        }
        return false; // 찾지 못한 경우 false 반환
    }

    private String findAddressName(String text, String type) {
        Pattern pattern = Pattern.compile("\\b\\S*" + type + "\\b");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public String formattedAddress(){
        String[] arr = jibunAddress.split(" ");
        return arr[0] + " " + arr[1] + " " + arr[2];
    }

    public static Map<Integer, Mungple> listToMap(List<Mungple> mungpleList) {
        return mungpleList.stream().collect(Collectors.toMap(Mungple::getMungpleId, Function.identity()));
    }

    public String getAddressForPush() {
        return findAddressName(jibunAddress, isExistGu(jibunAddress) ? "구" : "시");
    }

    public String getThumbnailUrl() {
        return photoUrls.get(0);
    }
}