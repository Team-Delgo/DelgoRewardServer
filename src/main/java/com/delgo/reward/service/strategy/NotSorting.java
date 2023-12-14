package com.delgo.reward.service.strategy;

import com.delgo.reward.mongoDomain.mungple.Mungple;

import java.util.List;

public class NotSorting implements MungpleSortingStrategy {
    private final List<Mungple> mungpleList;

    public NotSorting(List<Mungple> mungpleList){
        this.mungpleList = mungpleList;
    }
    @Override
    public List<Mungple> sort() {
        return mungpleList;
    }
}
