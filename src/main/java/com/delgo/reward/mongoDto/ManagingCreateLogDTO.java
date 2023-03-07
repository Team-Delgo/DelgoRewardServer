package com.delgo.reward.mongoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagingCreateLogDTO {
    private String controllerName;
    private String methodName;
    private Map<String, Object> args;
    private LocalDateTime createAt;
}
