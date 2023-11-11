package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.user.service.BookmarkService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
public class BookmarkController extends CommController {
    private final BookmarkService bookmarkService;

    /**
     * 멍플 Bookmark
     * @param userId, mungpleId
     */
    @PostMapping(value = "/{userId}/{mungpleId}")
    public ResponseEntity bookmark(@PathVariable Integer userId, @PathVariable Integer mungpleId){
        return SuccessReturn(bookmarkService.bookmark(userId, mungpleId));
    }

}
