package com.delgo.reward.service.strategy;

import com.delgo.reward.mongoDomain.mungple.Mungple;
import java.util.List;

public interface MungpleSortingStrategy {
    List<Mungple> sort();
}