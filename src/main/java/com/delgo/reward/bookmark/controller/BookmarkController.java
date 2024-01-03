package com.delgo.reward.bookmark.controller;

import com.delgo.reward.common.controller.CommController;
import com.delgo.reward.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
public class BookmarkController extends CommController {
    private final BookmarkService bookmarkService;

    @PostMapping(value = "/{userId}/{mungpleId}")
    public ResponseEntity bookmark(@PathVariable Integer userId, @PathVariable Integer mungpleId){
        return SuccessReturn(bookmarkService.bookmark(userId, mungpleId));
    }
}
