package com.delgo.reward.mongoDomain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import java.time.LocalTime;
import java.util.Map;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="detail_data")
public class MungpleDetailData {
    @Id
    private String id;
    @Field("mungple_id")
    private int mungpleId;
    @Field("address")
    private String address;
    @Field("business_hour")
    private Map<String, LocalTime> businessHour;
    @Field("tel_number")
    private String telNumber;
    @Field("is_resident_dog")
    private boolean isResidentDog;
    @Field("is_restriction")
    private Map<String, Boolean> restriction;
    @Field("accept_size")
    private Map<String, Boolean> acceptSize;
    @Field("represent_menu_title")
    private Map<Integer, String> representMenuTitle;
    @Field("represent_menu_photo_url")
    private Map<Integer, String> representMenuPhotoUrl;
    @Field("is_parking")
    private boolean isParking;
    @Field("parking_info")
    private Map<String, String> parkingInfo;
}
