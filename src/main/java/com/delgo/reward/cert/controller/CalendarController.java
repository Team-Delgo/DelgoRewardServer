package com.delgo.reward.cert.controller;

import com.delgo.reward.cert.response.CertResponse;
import com.delgo.reward.common.controller.CommController;
import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.domain.Reaction;
import com.delgo.reward.cert.response.CalendarResponse;
import com.delgo.reward.cert.service.ReactionService;
import com.delgo.reward.cert.service.CertQueryService;
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

        List<CertResponse> CertResponseList = CertResponse.fromList(userId, page.getContent(), reactionMap);
        return SuccessReturn(CalendarResponse.from(CertResponseList));
    }
}