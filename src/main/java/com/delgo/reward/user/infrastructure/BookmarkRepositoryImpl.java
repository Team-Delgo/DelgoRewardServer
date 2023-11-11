package com.delgo.reward.user.infrastructure;

import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.user.infrastructure.entity.BookmarkEntity;
import com.delgo.reward.user.infrastructure.jpa.BookmarkJpaRepository;
import com.delgo.reward.user.service.port.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepository {
    private final BookmarkJpaRepository bookmarkJpaRepository;

    @Override
    public Boolean existsByUserIdAndMungpleId(Integer userId, Integer mungpleId){
        return bookmarkJpaRepository.existsByUserIdAndMungpleId(userId, mungpleId);
    }

    @Override
    public Boolean existsByUserIdAndMungpleIdAndIsBookmarked(Integer userId, Integer mungpleId, boolean isBookmarked) {
        return bookmarkJpaRepository.existsByUserIdAndMungpleIdAndIsBookmarked(userId, mungpleId, isBookmarked);
    }

    @Override
    public Optional<BookmarkEntity> findByUserIdAndMungpleId(Integer userId, Integer mungpleId) {
        return Optional.empty();
    }

    @Override
    public List<BookmarkEntity> findActiveBookmarkByUserId(Integer userId) {
        return null;
    }

    @Override
    public Set<Integer> findBookmarkedMungpleIds(Integer userId) {
        return null;
    }

    @Override
    public int countOfActiveBookmarkByMungple(int mungpleId) {
        return 0;
    }

    @Override
    public List<MungpleCountDTO> countBookmarksGroupedByMungpleId() {
        return null;
    }

}
