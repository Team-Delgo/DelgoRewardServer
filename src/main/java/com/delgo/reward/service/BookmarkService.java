package com.delgo.reward.service;

import com.delgo.reward.domain.user.Bookmark;
import com.delgo.reward.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    /**
     * [userId, mungpleId] 북마크 등록 또는 제거
     */
    public Bookmark bookmark (int userId, int mungpleId) {
        if (hasBookmark(userId, mungpleId)) {
            return getBookmark(userId, mungpleId).setIsBookmarkedReverse();
        } else {
            return bookmarkRepository.save(Bookmark.builder()
                    .userId(userId)
                    .mungpleId(mungpleId)
                    .isBookmarked(true)
                    .build());
        }
    }

    /**
     * [userId, mungpleId] 북마크 존재 여부 반환
     */
    public Boolean hasBookmark(int userId, int mungpleId) {
        return bookmarkRepository.existsByUserIdAndMungpleId(userId, mungpleId);
    }

    /**
     * [userId, mungpleId, isBookmarked] 북마크 데이터가 존재하고 북마크가 되어있는 지 여부 반환
     */
    public Boolean hasBookmarkByIsBookmarked(int userId, int mungpleId, boolean isBookmarked){
        return bookmarkRepository.existsByUserIdAndMungpleIdAndIsBookmarked(userId, mungpleId, isBookmarked);
    }


    /**
     * [userId, mungpleId] 북마크 가져오기
     */
    public Bookmark getBookmark(int userId, int mungpleId) {
        return bookmarkRepository.findByUserIdAndMungpleId(userId, mungpleId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND Bookmark userId : " + userId + " mungpleId: " + mungpleId));
    }
}
