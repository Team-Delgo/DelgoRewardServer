package com.delgo.reward.comm.security.config;

public interface RefreshTokenProperties {
	Long EXPIRATION_TIME = 60000L * 60 * 24 * 7 * 365; // 1분 * 60 * 24 * 7 = 일주일 * 365
	String HEADER_STRING = "Authorization_Refresh";
}
