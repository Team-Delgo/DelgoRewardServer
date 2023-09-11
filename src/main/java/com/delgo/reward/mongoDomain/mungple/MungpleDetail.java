package com.delgo.reward.mongoDomain.mungple;

import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.DetailCode;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="mungple_detail")
public class MungpleDetail {
    // Common
    @Id
    private String id;
    @Field("mungple_id")
    private int mungpleId;

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

    @Field("editor_note_url")
    private String editorNoteUrl; // 에디터 노트 URL
    @Field("copy_link")
    private String copyLink;

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
}