package com.delgo.reward.cache;

import com.delgo.reward.mongoDomain.mungple.Mungple;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MungpleCache {
    private Mungple mungple;
    private LocalDateTime expirationDate;
}
