package com.delgo.reward.dto;


import com.delgo.reward.domain.Certification;
import com.delgo.reward.domain.Code;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificationDTO {
    private Integer userId;
    private String categoryCode;

    private Integer mungpleId; // mungpleId == 0이면 mungple 장소 아님.
    private String placeName; // 장소 명
    private String description; // 내용

    private String latitude; // 위도
    private String longitude; // 경도

//    private MultipartFile photo; // 사진 파일

    public Certification makeCertification(Code code, String photoUrl) {
        // TODO : REVERSE;
        return Certification.builder()
                .userId(this.userId)
                .categoryCode(this.categoryCode)
                .mungpleId(this.mungpleId)
                .placeName(this.placeName)
                .description(this.description)
                .geoCode(code.getCode()) // 사용자 기준 geoCode
                .p_geoCode(code.getPCode()) // 사용자 기준 geoCode
                .latitude(this.latitude) // 사용자한테 입력받은 위도
                .longitude(this.longitude) // 사용자한테 입력받은 경도
                .photoUrl(photoUrl)
                .isPhotoChecked(0)
                .build();
    }

}