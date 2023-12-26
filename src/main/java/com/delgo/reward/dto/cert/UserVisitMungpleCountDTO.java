package com.delgo.reward.dto.cert;

import com.delgo.reward.mongoDomain.mungple.Mungple;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
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

    public static List<UserVisitMungpleCountDTO> setMungpleData(List<UserVisitMungpleCountDTO> dtoList, Map<Integer,
            Mungple> mungpleMap) {
        try {
            return dtoList.stream().map(dto -> {
                Mungple mungple = mungpleMap.get(dto.getMungpleId());
                return UserVisitMungpleCountDTO.builder()
                        .mungpleId(dto.getMungpleId())
                        .visitCount(dto.getVisitCount())
                        .placeName(mungple.getPlaceName())
                        .photoUrl(mungple.getPhotoUrls().get(0))
                        .build();
            }).toList();
        } catch (Exception e) {
            log.error("[UserVisitMungpleCountDTO.setMungpleData ] : {}" ,e.getMessage());
            return new ArrayList<>();
        }
    }
}
