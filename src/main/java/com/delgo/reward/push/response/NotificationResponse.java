package com.delgo.reward.push.response;

import com.delgo.reward.push.domain.Notification;
import com.delgo.reward.push.domain.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class NotificationResponse {
    @Schema(description = "알림 고유 번호")
    private Integer notificationId;
    @Schema(description = "알림 받은 userId")
    private Integer userId;
    @Schema(description = "알림 메시지")
    private String message;
    @Schema(description = "알림 타입", enumAsRef = true)
    private NotificationType notificationType;
    @Schema(description = "cert 관련 이면 certificationId , mungple 관련 이면 mungpleId을 뜻한다.")
    private Integer objectId;
    @Schema(description = "사용자 확인 여부")
    private Boolean isRead;
    @Schema(description = "발송 날짜")
    @JsonFormat(pattern = "yyyy.MM.dd/HH:mm/E")
    private LocalDateTime createAt;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .userId(notification.getUserId())
                .message(notification.getMessage())
                .notificationType(notification.getNotificationType())
                .objectId(notification.getObjectId())
                .isRead(notification.getIsRead())
                .createAt(notification.getCreateAt())
                .build();
    }

    public static List<NotificationResponse> fromList(List<Notification> notificationList) {
        return notificationList.stream().map(NotificationResponse::from).toList();
    }
}