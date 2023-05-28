package com.delgo.reward.record.mungple;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public record MungpleDetailDataRecord(
        int mungpleId,
        String address,
        Map<String, LocalTime> businessHour,
        String telNumber,
        boolean isResidentDog,
        Map<String, Boolean> restriction,
        Map<String, Boolean> acceptSize,
        List<String> representMenuTitleList,
        boolean isParking,
        Map<String, String> parkingInfo
) {
}
