package com.delgo.reward.repository;

import com.delgo.reward.domain.user.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Boolean existsByUserIdAndMungpleId(Integer userId, Integer mungpleId);
    Boolean existsByUserIdAndMungpleIdAndIsBookmarked(Integer userId, Integer mungpleId, boolean isBookmarked);
    Optional<Bookmark> findByUserIdAndMungpleId(Integer userId, Integer mungpleId);
    List<Bookmark> findByUserId(Integer userId);
}
