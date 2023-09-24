package com.delgo.reward.repository;

import com.delgo.reward.domain.user.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Boolean existsByUserIdAndMungpleId(Integer userId, Integer mungpleId);
    Boolean existsByUserIdAndMungpleIdAndIsBookmarked(Integer userId, Integer mungpleId, boolean isBookmarked);
    Optional<Bookmark> findByUserIdAndMungpleId(Integer userId, Integer mungpleId);

    @Query("SELECT b FROM Bookmark b WHERE b.userId = :userId and b.isBookmarked = true")
    List<Bookmark> findActiveBookmarkByUserId(Integer userId);

    @Query(value = "select count(b) from Bookmark b where b.mungpleId = :mungpleId and b.isBookmarked = true")
    int countOfActiveBookmarkByMungple(int mungpleId);
}
