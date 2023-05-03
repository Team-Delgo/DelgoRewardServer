package com.delgo.reward.dto.cert;


import com.delgo.reward.domain.certification.Certification;
import lombok.*;


@Getter
@ToString
public class CertByMungpleResDTO extends CertResDTO {
    private final Integer mungpleId;

    public CertByMungpleResDTO(Certification certification, int userId) {
        super(certification, userId);
        mungpleId = certification.getMungpleId();
    }
}