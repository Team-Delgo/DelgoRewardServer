package com.delgo.reward.comm.security.config;

public interface AccessTokenProperties {
	int EXPIRATION_TIME = 60000 * 30; // 1분 * 30 = 30분
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "Authorization_Access";
}
