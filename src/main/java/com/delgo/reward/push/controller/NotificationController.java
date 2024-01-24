package com.delgo.reward.push.controller;

import com.delgo.reward.common.controller.CommController;
import com.delgo.reward.push.domain.Notification;
import com.delgo.reward.push.response.NotificationResponse;
import com.delgo.reward.push.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController extends CommController {
    private final NotificationService notificationService;

    @Operation(summary = " User 별 알림 목록 조회", description = " notifyType에 따라 objectId가 다른 의미 " +
            "<br> * Comment, Cute, Helper = certificationId ( 동적 라우팅으로 /cert/{certificationId}로 이동 ) " +
            "<br> * Mungple = mungpleId ( 동적 라우팅으로 /detail/{mungpleId}로 이동 )")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NotificationResponse.class))})
    @GetMapping
    public ResponseEntity<?> getListByUserId(@RequestParam(name = "userId") int userId) {
        List<Notification> notificationList = notificationService.getListByUserId(userId);
        notificationService.read(notificationList);

        return SuccessReturn(NotificationResponse.fromList(notificationList));
    }

    @Operation(summary = "새로운 알림 여부 체크", description = "- true = 새로운 알림 존재 0 <br> false = 새로운 알림 존재 X")
    @GetMapping("/new")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))})
    public ResponseEntity<?> checkNew(@RequestParam(name = "userId") int userId) {
        return SuccessReturn(notificationService.hasUnreadNotification(userId));
    }
}
