package com.delgo.reward.dto.cert;


import com.delgo.reward.domain.certification.Certification;
import lombok.*;


@Getter
@ToString
public class CertByMungpleResDTO extends CertResDTO {
    private final Integer mungpleId;

    public CertByMungpleResDTO(Certification certification) {
        super(certification);
        mungpleId = certification.getMungpleId();
    }
}