package com.delgo.reward.dto.cert;

import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.domain.certification.Certification;
import lombok.*;

import java.util.List;

@Getter
@ToString
public class CertByAchvResDTO extends CertResDTO {
    private  Boolean isAchievements; // 업적 영향 여부 ( 해당 인증이 등록되었을 때 가지게 된 업적이 있는가?)
    private  List<Achievements> achievements;

    public CertByAchvResDTO(Certification certification) {
        super(certification);
        this.isAchievements = false;
    }

    public CertByAchvResDTO setAchievements(List<Achievements> achievements){
        this.isAchievements = true;
        this.achievements = achievements;

        return this;
    }
}