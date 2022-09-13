package com.delgo.reward.comm.security.jwt;

public interface Access_JwtProperties {
	String SECRET = "Access_Delgo_Reward_SecretKey"; // Key 값
//	int EXPIRATION_TIME = 600000; // 10분
//	int EXPIRATION_TIME = 60000; // 1분
	int EXPIRATION_TIME = 10000; // 1분
	String TOKEN_PREFIX = "Access ";
	String HEADER_STRING = "Authorization_Access";
}
