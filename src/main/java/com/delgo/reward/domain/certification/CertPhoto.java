package com.delgo.reward.domain.certification;

import com.delgo.reward.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertPhoto extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
