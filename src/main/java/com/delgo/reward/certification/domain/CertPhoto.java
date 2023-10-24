package com.delgo.reward.certification.domain;

import lombok.*;


@Getter
@Builder
@AllArgsConstructor
public class CertPhoto {
    private Integer certPhotoId;
    private Integer certificationId;
    private String url;
    private Boolean isCorrect;

    public static CertPhoto from(int certificationId, String url) {
        return CertPhoto.builder()
                .certificationId(certificationId)
                .url(url)
                .isCorrect(true)
                .build();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
