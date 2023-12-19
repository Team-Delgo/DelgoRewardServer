package com.delgo.reward.cache;

import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MungpleCache {
    private MongoMungple mongoMungple;
    private LocalDateTime expirationDate;
}