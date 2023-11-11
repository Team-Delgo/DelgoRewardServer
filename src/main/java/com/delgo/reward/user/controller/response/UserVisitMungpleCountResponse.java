package com.delgo.reward.user.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVisitMungpleCountResponse {
    @Schema(description = "멍플 고유 아이디")
    protected Integer mungpleId;
    @Schema(description = "방문 횟수")
    protected Long visitCount;
    @Schema(description = "장소 이름")
    protected String placeName;
    @Schema(description = "사진 URL")
    protected String photoUrl;

    public UserVisitMungpleCountResponse setMungpleData(String placeName, String photoUrl){
        this.placeName = placeName;
        this.photoUrl = photoUrl;
        return this;
    }
}
