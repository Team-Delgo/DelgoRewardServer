package com.delgo.reward.bookmark.repository;

import com.delgo.reward.bookmark.domain.Bookmark;
import com.delgo.reward.cert.repository.dto.MungpleCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Optional<Bookmark> findOneByUserIdAndMungpleId(Integer userId, Integer mungpleId);
    Boolean existsByUserIdAndMungpleIdAndIsBookmarked(Integer userId, Integer mungpleId, boolean isBookmarked);

    @Query("SELECT b FROM Bookmark b WHERE b.userId = :userId and b.isBookmarked = true")
    List<Bookmark> findActiveListByUserId(@Param("userId") Integer userId);

    @Query(value = "select count(b) from Bookmark b where b.mungpleId = :mungpleId and b.isBookmarked = true")
    int countActiveListByMungpleId(@Param("mungpleId")int mungpleId);

    @Query(value = "select new com.delgo.reward.cert.repository.dto.MungpleCountDTO(b.mungpleId, count(b)) from Bookmark b where b.isBookmarked = true group by b.mungpleId order by count(b) desc")
    List<MungpleCountDTO> countActiveListGroupedByMungpleId();
}
