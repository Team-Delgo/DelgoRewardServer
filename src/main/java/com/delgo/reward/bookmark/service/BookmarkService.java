package com.delgo.reward.bookmark.service;

import com.delgo.reward.bookmark.domain.Bookmark;
import com.delgo.reward.cert.repository.dto.MungpleCountDTO;
import com.delgo.reward.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public Bookmark bookmark(int userId, int mungpleId) {
        return bookmarkRepository.findOneByUserIdAndMungpleId(userId, mungpleId)
                .map(bookmark -> bookmarkRepository.save(bookmark.update())) // 값이 있다면 Update
                .orElseGet(() -> bookmarkRepository.save(Bookmark.from(userId, mungpleId))); // 없다면 생성
    }

    public List<Bookmark> getListByUserId(int userId) {
        return bookmarkRepository.findActiveListByUserId(userId);
    }

    public Boolean isBookmarked(int userId, int mungpleId){
        return bookmarkRepository.existsByUserIdAndMungpleIdAndIsBookmarked(userId, mungpleId, true);
    }

    public int getCountByMungpleId(int mungpleId) {
        return bookmarkRepository.countActiveListByMungpleId(mungpleId);
    }

    public Map<Integer, Integer> getCountMapByMungple(){
        return bookmarkRepository.countActiveListGroupedByMungpleId().stream()
                .collect(Collectors.toMap(MungpleCountDTO::getMungpleId, MungpleCountDTO::getCount));
    }
}
