package com.delgo.reward.dto.cert;

import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.domain.certification.Certification;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class CertByAchvResDTO extends CertResDTO {
    private final Boolean isAchievements; // 업적 영향 여부 ( 해당 인증이 등록되었을 때 가지게 된 업적이 있는가?)
    private final List<Achievements> achievements;

    public CertByAchvResDTO(Certification certification) {
        super(certification);
        isAchievements = certification.getIsAchievements();
        achievements = certification.getAchievements();
    }
}