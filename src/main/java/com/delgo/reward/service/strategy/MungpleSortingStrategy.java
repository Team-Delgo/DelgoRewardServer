package com.delgo.reward.service.strategy;

import com.delgo.reward.domain.user.Bookmark;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;

import java.util.List;

public interface MungpleSortingStrategy {
    List<MongoMungple> sort(List<MongoMungple> mungpleList);
    List<MongoMungple> sortByBookmark(List<Bookmark> bookmarks);
}