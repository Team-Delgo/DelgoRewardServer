package com.delgo.reward.common.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseRecord<T>(int code, String codeMsg, T data) {}