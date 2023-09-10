package com.delgo.reward.mongoDomain;


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

}
