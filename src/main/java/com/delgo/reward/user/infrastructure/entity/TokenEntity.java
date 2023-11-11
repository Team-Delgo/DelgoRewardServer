package com.delgo.reward.user.infrastructure.entity;

import com.delgo.reward.domain.common.BaseTimeEntity;
import com.delgo.reward.user.domain.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "token")
public class TokenEntity extends BaseTimeEntity {
    @Id
    private Integer userId;
    private String refreshToken;
    private String fcmToken;

    public Token toModel(){
        return Token
                .builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .fcmToken(fcmToken)
                .build();
    }
}
