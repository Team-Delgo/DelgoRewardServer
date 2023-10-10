package com.delgo.reward.dto.cert;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.domain.certification.CertPhoto;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.like.LikeList;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CertResDTO {
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
    @Schema(description = "사진 URL 리스트")
    protected List<String> photos;
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
    @Schema(description = "조회한 유저의 좋아요 여부 [Deprecated] - reaction 대체 됨")
    protected Boolean isLike;
    @Schema(description = "좋아요 개수 [Deprecated] - reaction 대체 됨")
    protected int likeCount;
    @Schema(description = "댓글 개수")
    protected int commentCount;

    @Schema(description = "조회한 유저의 반응 여부")
    protected Map<ReactionCode, Boolean> reactionMap;
    @Schema(description = "리액션 별 개수")
    protected Map<ReactionCode, Integer> reactionCountMap;

    @Schema(description = "위도")
    protected String latitude;
    @Schema(description = "경도")
    protected String longitude;

    @JsonFormat(pattern = "yyyy.MM.dd/HH:mm/E")
    @Schema(description = "등록 날짜")
    protected LocalDateTime registDt;


    public CertResDTO(Certification certification, Integer ownerId) {
        this(certification);
        isOwner = certification.getUser().getUserId() == ownerId;

        if (certification.getLikeLists() != null) {
            isLike = certification.getLikeLists().stream().anyMatch(likeList -> likeList.getUserId().equals(ownerId) && likeList.isLike());
            likeCount = (int) certification.getLikeLists().stream().filter(LikeList::isLike).count();
        } else {
            isLike = false;
            likeCount = 0;
        }

        if (reactionMap == null) {
            reactionMap = ReactionCode.initializeReactionMap();
        }
        if (reactionCountMap == null) {
            reactionCountMap = ReactionCode.initializeReactionCountMap();
        }

        if (certification.getReactionList() != null) {
            ReactionCode.setReactionMapByUserId(reactionMap, certification.getReactionList(), ownerId);
            ReactionCode.setReactionCountMap(reactionCountMap, certification.getReactionList());
        }
    }

    public CertResDTO(Certification cert) {
        certificationId = cert.getCertificationId();
        categoryCode = cert.getCategoryCode();
        mungpleId = cert.getMungpleId();
        placeName = cert.getPlaceName();
        description = cert.getDescription();
        address = cert.getAddress();
        photos = cert.getPhotos().stream().map(CertPhoto::getUrl).toList();
        isHideAddress = cert.getIsHideAddress();
        userId = cert.getUser().getUserId();
        userName = cert.getUser().getName();
        userProfile = cert.getUser().getProfile();
        commentCount = cert.getCommentCount();

        latitude = cert.getLatitude();
        longitude = cert.getLongitude();

        registDt = cert.getRegistDt();
    }
}