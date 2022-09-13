package com.delgo.reward.comm.security.jwt;

public interface Refresh_JwtProperties {
	String SECRET = "Refresh_Delgo_Reward_SecretKey"; // Key 값
	int EXPIRATION_TIME = 864000000; // 10일 (1/1000초)
	String TOKEN_PREFIX = "Refresh ";
	String HEADER_STRING = "Authorization_Refresh";
}
