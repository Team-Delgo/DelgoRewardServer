package com.delgo.reward.comm.security.jwt;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class JwtToken {
    private int userId;
    private String accessToken;
    private String refreshToken;
}
