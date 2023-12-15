package com.delgo.reward.repository;

import com.delgo.reward.domain.user.Bookmark;
import com.delgo.reward.dto.mungple.MungpleCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Boolean existsByUserIdAndMungpleId(Integer userId, Integer mungpleId);
    Boolean existsByUserIdAndMungpleIdAndIsBookmarked(Integer userId, Integer mungpleId, boolean isBookmarked);
    Optional<Bookmark> findByUserIdAndMungpleId(Integer userId, Integer mungpleId);

    @Query("SELECT b FROM Bookmark b WHERE b.userId = :userId and b.isBookmarked = true")
    List<Bookmark> findActiveBookmarkByUserId(@Param("userId") Integer userId);

    @Query("SELECT b.mungpleId FROM Bookmark b WHERE b.userId = :userId and b.isBookmarked = true")
    Set<Integer> findBookmarkedMungpleIds(@Param("userId") Integer userId);

    @Query(value = "select count(b) from Bookmark b where b.mungpleId = :mungpleId and b.isBookmarked = true")
    int countOfActiveBookmarkByMungple(@Param("mungpleId")int mungpleId);

    @Query(value = "select new com.delgo.reward.dto.mungple.MungpleCountDTO(b.mungpleId, count(b)) from Bookmark b where b.isBookmarked = true group by b.mungpleId order by count(b) desc")
    List<MungpleCountDTO> countBookmarksGroupedByMungpleId();
}
