package com.delgo.reward.cache;

import com.delgo.reward.comm.code.CategoryCode;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityCache {
    private Map<CategoryCode, Integer> activityMapByCategoryCode;
    private LocalDateTime expirationDate;
}
