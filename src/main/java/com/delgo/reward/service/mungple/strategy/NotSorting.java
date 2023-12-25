package com.delgo.reward.service.mungple.strategy;

import com.delgo.reward.mongoDomain.mungple.Mungple;
import java.util.List;

public class NotSorting implements MungpleSortingStrategy{
    @Override
    public List<Mungple> sort(List<Mungple> mungpleList) {
      return mungpleList;
    }
}
