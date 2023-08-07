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
}
