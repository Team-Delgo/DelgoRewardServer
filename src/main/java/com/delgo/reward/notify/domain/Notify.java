package com.delgo.reward.notify.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@ToString
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
    @CreationTimestamp
    private LocalDateTime createAt;

    public Notify toEntity(int userId, NotifyType notifyType, String notifyMsg){
        return Notify.builder()
                .userId(userId)
                .notifyType(notifyType)
                .notifyMsg(notifyMsg)
                .createAt(LocalDateTime.now())
                .build();

    }
}
