package com.delgo.reward.mungple.service.strategy;

import com.delgo.reward.mungple.domain.Mungple;
import java.util.List;

public interface MungpleSortingStrategy {
    List<Mungple> sort(List<Mungple> mungpleList);
}