package com.delgo.reward.dto.cert;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.certification.Reaction;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertResponse {
    @Schema(description = "인증 고유 번호")
    protected Integer certificationId;
    @Schema(description = "카테고리 코드", enumAsRef = true)
    protected CategoryCode categoryCode;
    @Schema(description = "멍플 인증일 경우 고유 번호 (멍플X = 0)")
    protected int mungpleId;
    @Schema(description = "장소 명")
    protected String placeName;
    @Schema(description = "내용")
    protected String description;
    @Schema(description = "주소")
    protected String address;
    @Schema(description = "주소 숨김 여부")
    protected Boolean isHideAddress;
    @Schema(description = "작성자 ID")
    protected Integer userId;
    @Schema(description = "작성자 이름")
    protected String userName;
    @Schema(description = "작성자 프로필")
    protected String userProfile;
    @Schema(description = "작성자 인지 여부")
    protected Boolean isOwner;
    @Schema(description = "댓글 개수")
    protected int commentCount;

    @Schema(description = "위도")
    protected String latitude;
    @Schema(description = "경도")
    protected String longitude;

    @Schema(description = "사진 URL 리스트")
    protected List<String> photos;

    @Schema(description = "조회한 유저의 반응 여부")
    protected Map<ReactionCode, Boolean> reactionMap;
    @Schema(description = "리액션 별 개수")
    protected Map<ReactionCode, Integer> reactionCountMap;

    @JsonFormat(pattern = "yyyy.MM.dd/HH:mm/E")
    @Schema(description = "등록 날짜")
    protected LocalDateTime registDt;

    public static CertResponse from(Integer userId, Certification cert, List<Reaction> reactionList) {
        Map<ReactionCode, Boolean> reactionMap = ReactionCode.initializeReactionMap();
        Map<ReactionCode, Integer> reactionCountMap = ReactionCode.initializeReactionCountMap();
        if (!reactionList.isEmpty()) {
            ReactionCode.setReactionMapByUserId(reactionMap, reactionList, userId);
            ReactionCode.setReactionCountMap(reactionCountMap, reactionList);
        }

        return CertResponse.builder()
                .certificationId(cert.getCertificationId())
                .categoryCode(cert.getCategoryCode())
                .mungpleId(cert.getMungpleId())
                .placeName(cert.getPlaceName())
                .description(cert.getDescription())
                .address(cert.getAddress())
                .photos(cert.getPhotos())
                .isHideAddress(cert.getIsHideAddress())
                .userId(cert.getUser().getUserId())
                .userName(cert.getUser().getName())
                .userProfile(cert.getUser().getProfile())
                .commentCount(cert.getCommentCount())
                .latitude(cert.getLatitude())
                .longitude(cert.getLongitude())
                .registDt(cert.getRegistDt())
                .isOwner(cert.getUser().getUserId() == userId)
                .reactionMap(reactionMap)
                .reactionCountMap(reactionCountMap)
                .build();
    }

    // 분류를 위해 필요
    public static CertResponse from(Certification cert) {
        return CertResponse.builder()
                .certificationId(cert.getCertificationId())
                .categoryCode(cert.getCategoryCode())
                .mungpleId(cert.getMungpleId())
                .placeName(cert.getPlaceName())
                .description(cert.getDescription())
                .address(cert.getAddress())
                .photos(cert.getPhotos())
                .isHideAddress(cert.getIsHideAddress())
                .userId(cert.getUser().getUserId())
                .userName(cert.getUser().getName())
                .userProfile(cert.getUser().getProfile())
                .commentCount(cert.getCommentCount())
                .latitude(cert.getLatitude())
                .longitude(cert.getLongitude())
                .registDt(cert.getRegistDt())
                .build();
    }

    public static List<CertResponse> fromList(Integer userId, List<Certification> certList, Map<Integer, List<Reaction>> reactionMap) {
        return certList.stream().map(cert -> {
            List<Reaction> reactionList = reactionMap.getOrDefault(cert.getCertificationId(), Collections.emptyList());
            return CertResponse.from(userId, cert, reactionList);
        }).toList();
    }
}