package com.delgo.reward.service.strategy;

import com.delgo.reward.mongoDomain.mungple.MongoMungple;

import java.util.List;

public class NotSorting implements MungpleSortingStrategy {
    private final List<MongoMungple> mungpleList;

    public NotSorting(List<MongoMungple> mungpleList){
        this.mungpleList = mungpleList;
    }
    @Override
    public List<MongoMungple> sort() {
        return mungpleList;
    }
}
