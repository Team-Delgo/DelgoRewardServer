package com.delgo.reward.bookmark.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class BookmarkControllerTest {
    @Autowired
    private MockMvc mvc;

    private final String BASE_URL = "/api/bookmark";

    @Test
    @Transactional
    void bookmark_새로_생성() throws Exception {
        // given
        int userId = 1;
        int mungpleId = 3;

        // when
        mvc.perform(post(BASE_URL + "/" + userId + "/" + mungpleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.mungpleId").value(3))
                .andExpect(jsonPath("$.data.isBookmarked").value(true));

        // then
    }

    @Test
    @Transactional
    void bookmark_업데이트() throws Exception {
        // given
        int userId = 1;
        int mungpleId = 1;

        // when
        mvc.perform(post(BASE_URL + "/" + userId + "/" + mungpleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.mungpleId").value(mungpleId))
                .andExpect(jsonPath("$.data.isBookmarked").value(false));

        // then
    }
}