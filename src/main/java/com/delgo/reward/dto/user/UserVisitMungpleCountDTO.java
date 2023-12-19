package com.delgo.reward.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
public class UserVisitMungpleCountDTO {
    @Schema(description = "멍플 고유 아이디")
    private Integer mungpleId;
    @Schema(description = "방문 횟수")
    private Long visitCount;
    @Schema(description = "장소 이름")
    private String placeName;
    @Schema(description = "사진 URL")
    private String photoUrl;

    public UserVisitMungpleCountDTO(Integer mungpleId, Long visitCount){
        this.mungpleId = mungpleId;
        this.visitCount = visitCount;
    }

    public UserVisitMungpleCountDTO setMungpleData(String placeName, String photoUrl){
        this.placeName = placeName;
        this.photoUrl = photoUrl;
        return this;
    }
}