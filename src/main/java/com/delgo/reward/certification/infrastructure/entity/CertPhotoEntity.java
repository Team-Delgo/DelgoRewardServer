package com.delgo.reward.certification.infrastructure.entity;

import com.delgo.reward.certification.domain.CertPhoto;
import com.delgo.reward.domain.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cert_photo")
public class CertPhotoEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer certPhotoId;
    private Integer certificationId;
    private String url;
    private Boolean isCorrect;

    public CertPhoto toModel() {
        return CertPhoto.builder()
                .certPhotoId(certPhotoId)
                .certificationId(certificationId)
                .url(url)
                .isCorrect(isCorrect)
                .build();
    }

    public static CertPhotoEntity from(CertPhoto certPhoto) {
        return CertPhotoEntity.builder()
                .certPhotoId(certPhoto.getCertPhotoId())
                .certificationId(certPhoto.getCertificationId())
                .url(certPhoto.getUrl())
                .isCorrect(certPhoto.getIsCorrect())
                .build();
    }
}
