package com.delgo.reward.user.service.port;

import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.user.infrastructure.entity.BookmarkEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookmarkRepository{
    Boolean existsByUserIdAndMungpleId(Integer userId, Integer mungpleId);
    Boolean existsByUserIdAndMungpleIdAndIsBookmarked(Integer userId, Integer mungpleId, boolean isBookmarked);
    Optional<BookmarkEntity> findByUserIdAndMungpleId(Integer userId, Integer mungpleId);
    List<BookmarkEntity> findActiveBookmarkByUserId(Integer userId);
    Set<Integer> findBookmarkedMungpleIds(Integer userId);
    int countOfActiveBookmarkByMungple(int mungpleId);
    List<MungpleCountDTO> countBookmarksGroupedByMungpleId();
}
