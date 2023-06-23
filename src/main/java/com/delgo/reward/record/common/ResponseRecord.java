package com.delgo.reward.record.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseRecord<T>(int code, String codeMsg, T data) {}