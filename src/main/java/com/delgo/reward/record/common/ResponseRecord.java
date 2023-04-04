package com.delgo.reward.record.common;

public record ResponseRecord<T>(int code, String codeMsg, T data) {}