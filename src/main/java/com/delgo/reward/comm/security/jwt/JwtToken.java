package com.delgo.reward.comm.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {
    private String accessToken;
    private String refreshToken;
}
