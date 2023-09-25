package com.delgo.reward.dto.cert;


import com.delgo.reward.domain.certification.Certification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@ToString
public class CertByMungpleResDTO extends CertResDTO {
    @Schema(description = "위도")
    private final String latitude; // 위도
    @Schema(description = "경도")
    private final String longitude; // 경도

    public CertByMungpleResDTO(Certification certification, int userId) {
        super(certification, userId);
        latitude = certification.getLatitude();
        longitude = certification.getLongitude();
    }
}