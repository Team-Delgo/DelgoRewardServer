package com.delgo.reward.dto.cert;


import com.delgo.reward.domain.certification.Certification;
import lombok.*;


@Getter
@ToString
public class CertByMungpleResDTO extends CertResDTO {
    private final String latitude; // 위도
    private final String longitude; // 경도

    public CertByMungpleResDTO(Certification certification, int userId) {
        super(certification, userId);
        latitude = certification.getLatitude();
        longitude = certification.getLongitude();
    }
}