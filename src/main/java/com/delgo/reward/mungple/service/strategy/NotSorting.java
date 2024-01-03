package com.delgo.reward.mungple.service.strategy;

import com.delgo.reward.mungple.domain.Mungple;
import java.util.List;

public class NotSorting implements MungpleSortingStrategy{
    @Override
    public List<Mungple> sort(List<Mungple> mungpleList) {
      return mungpleList;
    }
}
