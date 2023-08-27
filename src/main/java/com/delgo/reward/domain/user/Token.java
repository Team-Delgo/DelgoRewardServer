package com.delgo.reward.domain.user;

import com.delgo.reward.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token extends BaseTimeEntity {
    @Id
    private Integer userId;
    private String refreshToken;
    private String fcmToken;
}
