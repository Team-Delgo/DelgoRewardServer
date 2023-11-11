package com.delgo.reward.user.infrastructure.jpa;

import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.user.infrastructure.entity.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookmarkJpaRepository extends JpaRepository<BookmarkEntity, Integer> {
    Boolean existsByUserIdAndMungpleId(Integer userId, Integer mungpleId);
    Boolean existsByUserIdAndMungpleIdAndIsBookmarked(Integer userId, Integer mungpleId, boolean isBookmarked);
    Optional<BookmarkEntity> findByUserIdAndMungpleId(Integer userId, Integer mungpleId);

    @Query("SELECT b FROM Bookmark b WHERE b.userId = :userId and b.isBookmarked = true")
    List<BookmarkEntity> findActiveBookmarkByUserId(Integer userId);

    @Query("SELECT b.mungpleId FROM BookmarkEntity b WHERE b.userId = :userId and b.isBookmarked = true")
    Set<Integer> findBookmarkedMungpleIds(Integer userId);

    @Query(value = "select count(b) from BookmarkEntity b where b.mungpleId = :mungpleId and b.isBookmarked = true")
    int countOfActiveBookmarkByMungple(int mungpleId);

    @Query(value = "select new com.delgo.reward.dto.mungple.MungpleCountDTO(b.mungpleId, count(b)) from BookmarkEntity b where b.isBookmarked = true group by b.mungpleId order by count(b) desc")
    List<MungpleCountDTO> countBookmarksGroupedByMungpleId();
}
