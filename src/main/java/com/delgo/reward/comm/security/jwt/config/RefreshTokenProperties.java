package com.delgo.reward.comm.security.jwt.config;

public interface RefreshTokenProperties {
	String SECRET = "Refresh_Delgo_Reward_SecretKey"; // Key 값
	Long EXPIRATION_TIME = 60000L * 60 * 24 * 30; // 1분 * 60 * 24 * 30 = 30일
	String TOKEN_PREFIX = "Refresh ";
	String HEADER_STRING = "Authorization_Refresh";
}
