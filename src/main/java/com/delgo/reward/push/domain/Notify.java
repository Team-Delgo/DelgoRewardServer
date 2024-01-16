package com.delgo.reward.push.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notifyId;
    private int userId;
    private String notifyMsg;
    @Enumerated(EnumType.STRING)
    private NotifyType notifyType;
    private LocalDateTime createAt;

    public static Notify from(int userId, String notifyMsg, NotifyType notifyType, LocalDateTime createAt) {
        return Notify.builder()
                .userId(userId)
                .notifyMsg(notifyMsg)
                .notifyType(notifyType)
                .createAt(createAt)
                .build();
    }
}
