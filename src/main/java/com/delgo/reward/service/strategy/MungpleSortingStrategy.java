package com.delgo.reward.service.strategy;

import com.delgo.reward.mongoDomain.mungple.MongoMungple;

import java.util.List;

public interface MungpleSortingStrategy {
    List<MongoMungple> sort(List<MongoMungple> mungples);
}