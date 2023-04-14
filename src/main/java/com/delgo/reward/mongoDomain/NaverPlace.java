package com.delgo.reward.mongoDomain;


import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.common.Location;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="naver_place")
public class NaverPlace {
    @Id private String id;
    @Field("placeName") private String placeName;
    @Field("address") private String address;
    @Field("category") private String category;
    @Field("photos") private List<String> photos;

    public Mungple toMungple(Location location, String categoryCode) {
        return Mungple.builder()
                .categoryCode(categoryCode)
                .placeName(this.placeName)
                .placeNameEn("")
                .geoCode(location.getGeoCode())
                .pGeoCode(location.getPGeoCode())
                .roadAddress(location.getRoadAddress())
                .jibunAddress(location.getJibunAddress())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .isActive(false)
                .build();
    }

}
