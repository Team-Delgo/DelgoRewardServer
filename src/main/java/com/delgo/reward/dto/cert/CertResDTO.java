package com.delgo.reward.dto.cert;


import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.like.LikeList;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CertResDTO {
    private Integer certificationId;
    private String placeName; // 장소 명
    private String description; // 내용
    private String photoUrl; // 사진 URL
    private int commentCount; // 댓글 개수

    private Boolean isHideAddress; // 주소 숨김 여부
    private Boolean isOwner; // cert 작성자인가?
    private String address; // 주소


    private Integer userId; // 작성자 ID
    private String userName; // 작성자 이 름
    private String userProfile; // 작성자 프로필

    private Boolean isLike = false; // 내가 좋아요를 눌렀는가?
    private int likeCount = 0; // 좋아요 개수

    @JsonFormat(pattern="yyyy.MM.dd/HH:mm/E")
    private LocalDateTime registDt;

    public CertResDTO(Certification certification, Integer ownerId) {
        this(certification);
        if(certification.getLikeLists() != null) {
            isOwner = certification.getUser().getUserId() == ownerId;
            likeCount = (int) certification.getLikeLists().stream().filter(LikeList::isLike).count();
            isLike = certification.getLikeLists().stream().anyMatch(likeList -> likeList.getUserId().equals(ownerId) && likeList.isLike());
        }
    }

    public CertResDTO(Certification cert) {
        certificationId = cert.getCertificationId();
        userId = cert.getUser().getUserId();
        userName = cert.getUser().getName();
        userProfile = cert.getUser().getProfile();
        placeName = cert.getPlaceName();
        description = cert.getDescription();
        address =  cert.getAddress();
        isHideAddress =  cert.getIsHideAddress();
        photoUrl = cert.getPhotoUrl();
        commentCount = cert.getCommentCount();
        registDt = cert.getRegistDt();
    }
}