package com.delgo.reward.notify.domain;

import com.delgo.reward.token.domain.NotifyType;
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
    @Enumerated(EnumType.STRING)
    private NotifyType notifyType;
    private String notifyMsg;
    @JsonFormat(pattern="yyyy.MM.dd/HH:mm/E")
    @CreatedDate
    private LocalDateTime createAt;

    public static Notify from(int userId, NotifyType notifyType, String notifyMsg){
        return Notify.builder()
                .userId(userId)
                .notifyType(notifyType)
                .notifyMsg(notifyMsg)
                .build();
    }
}
