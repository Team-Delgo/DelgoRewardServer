package com.delgo.reward.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WriterDTO {
    private Integer userId;
    private String name;
    private String profile;
}
