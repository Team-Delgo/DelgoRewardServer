package com.delgo.reward.dto.cert;


import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
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
    private String userName;
    private String userProfile;

    private Boolean isLike; // 내가 좋아요를 눌렀는가?
    private int likeCount; // 좋아요 개수

    @JsonFormat(pattern="yyyy.MM.dd/HH:mm/E")
    private LocalDateTime createdDate;

    public CertResDTO(Certification certification) {
        certificationId = certification.getCertificationId();
        userId = certification.getUserId();
        placeName = certification.getPlaceName();
        description = certification.getDescription();
        address = certification.getAddress();
        photoUrl = certification.getPhotoUrl();
        commentCount = certification.getCommentCount();
        createdDate = certification.getCreatedDate();
    }

    public CertResDTO setUserAndLike(User user, Boolean isLike, Integer likeCount) {
        userName = user.getName();
        userProfile = user.getProfile();
        this.isLike = isLike;
        this.likeCount = likeCount;

        return this;
    }
}