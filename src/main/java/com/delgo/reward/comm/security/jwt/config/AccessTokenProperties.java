package com.delgo.reward.comm.security.jwt.config;

public interface AccessTokenProperties {
	String SECRET = "Access_Delgo_Reward_SecretKey"; // Key 값
	int EXPIRATION_TIME = 60000 * 30; // 1분 * 30 = 30분
	String TOKEN_PREFIX = "Access ";
	String HEADER_STRING = "Authorization_Access";
}
