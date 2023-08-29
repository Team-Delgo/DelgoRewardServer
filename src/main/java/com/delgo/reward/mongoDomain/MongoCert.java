package com.delgo.reward.mongoDomain;

import com.delgo.reward.comm.code.ReactionCode;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document("certification")
public class MongoCert {
    @Id
    private String id;
    @Field("category_code")
    private String categoryCode;
    @Field("mungple_id")
    private Integer mungpleId; // mungpleId == 0이면 mungple 장소 아님.
    @Field("place_name")
    private String placeName; // 장소 명
    @Field("description")
    private String description; // 내용
    @Field("address")
    private String address; // 주소
    @Field("geo_code")
    private String geoCode; // 지역 코드
    @Field("p_geo_code")
    private String pGeoCode; // 부모 지역 코드
    @Field("latitude")
    private String latitude; // 위도
    @Field("longitude")
    private String longitude; // 경도
    @Field("photo_url")
    private String photoUrl; // 사진 URL
    @Field("is_correct")
    private Boolean isCorrect; // 올바른 사진 여부 ( 파이썬 모듈로 체크 )
    @Field("is_achievements")
    private Boolean isAchievements; // 업적 영향 여부 ( 해당 인증이 등록되었을 때 가지게 된 업적이 있는가?)
    @Field("comment_count")
    private int commentCount; // 댓글 개수
    @Field("is_expose")
    private Boolean isExpose; // Map에 노출 시키는 인증 구분. ( 초기엔 운영진이 직접 추가 예정 )
    @Field("reaction_count")
    private Map<ReactionCode, Integer> reactionCount;
    @Field("reaction_list")
    private Map<ReactionCode, List<Integer>> reactionUserList;

    public MongoCert addOneReactionCount(ReactionCode reactionCode){
        this.reactionCount.put(reactionCode, this.reactionCount.get(reactionCode) + 1);
        return this;
    }

    public MongoCert minusOneReactionCount(ReactionCode reactionCode){
        this.reactionCount.put(reactionCode, this.reactionCount.get(reactionCode) - 1);
        if (this.reactionCount.get(reactionCode) < 0){
            this.reactionCount.put(reactionCode, 0);
        }
        return this;
    }
}
