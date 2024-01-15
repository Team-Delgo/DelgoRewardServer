package com.delgo.reward.push.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

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
    @CreatedDate @JsonFormat(pattern="yyyy.MM.dd/HH:mm/E")
    private LocalDateTime createAt;

    public static Notify from(int userId, String notifyMsg, NotifyType notifyType) {
        return Notify.builder()
                .userId(userId)
                .notifyMsg(notifyMsg)
                .notifyType(notifyType)
                .createAt(LocalDateTime.now())
                .build();
    }
}
