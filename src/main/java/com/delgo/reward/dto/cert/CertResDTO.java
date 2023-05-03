package com.delgo.reward.dto.cert;


import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.like.LikeList;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
public class CertResDTO {
    private Integer certificationId;
    private String placeName; // 장소 명
    private String description; // 내용
    private String address; // 주소
    private String photoUrl; // 사진 URL
    private int commentCount; // 댓글 개수

    private Integer userId; // 작성자 ID
    private String userName; // 작성자 이름
    private String userProfile; // 작성자 프로필

    private Boolean isLike; // 내가 좋아요를 눌렀는가?
    private int likeCount; // 좋아요 개수

    @JsonFormat(pattern="yyyy.MM.dd/HH:mm/E")
    private LocalDateTime createdDate;

    public CertResDTO(Certification certification, Integer ownerId) {
        certificationId = certification.getCertificationId();
        userId = certification.getUser().getUserId();
        userName = certification.getUser().getName();
        userProfile = certification.getUser().getProfile();
        placeName = certification.getPlaceName();
        description = certification.getDescription();
        address = certification.getAddress();
        photoUrl = certification.getPhotoUrl();
        commentCount = certification.getCommentCount();
        createdDate = certification.getCreatedDate();
        likeCount = (int) certification.getLikeLists().stream().filter(LikeList::isLike).count();
        isLike = certification.getLikeLists().stream().anyMatch(likeList -> likeList.getUserId().equals(ownerId) && likeList.isLike());
    }

    public CertResDTO(Certification certification) {
        certificationId = certification.getCertificationId();
        userId = certification.getUser().getUserId();
        userName = certification.getUser().getName();
        userProfile = certification.getUser().getProfile();
        placeName = certification.getPlaceName();
        description = certification.getDescription();
        address = certification.getAddress();
        photoUrl = certification.getPhotoUrl();
        commentCount = certification.getCommentCount();
        createdDate = certification.getCreatedDate();
    }
}