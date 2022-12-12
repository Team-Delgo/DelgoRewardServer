package com.delgo.reward.domain.user;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    private Integer userId;
    private String refreshToken;
    private String fcmToken;
}
