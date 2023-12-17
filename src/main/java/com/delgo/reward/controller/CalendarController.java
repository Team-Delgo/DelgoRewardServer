package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.certification.Reaction;
import com.delgo.reward.dto.cert.CalendarResponse;
import com.delgo.reward.service.ReactionService;
import com.delgo.reward.service.cert.CertQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController extends CommController {
    private final ReactionService reactionService;
    private final CertQueryService certQueryService;

    /*
     * 날짜별 인증 조회 [ 캘린더 ]
     * Request Data : userId
     * Response Data : 캘린더 인증 리스트 반환
     */
    @GetMapping("/{userId}")
    public ResponseEntity getCalendar(@PathVariable Integer userId) {
        Page<Certification> page = certQueryService.getPagingListByUserId(userId, Pageable.unpaged());
        Map<Integer, List<Reaction>> reactionMap = reactionService.getMapByCertList(page.getContent());

        return SuccessReturn(CalendarResponse.from(userId, page, reactionMap));
    }
}