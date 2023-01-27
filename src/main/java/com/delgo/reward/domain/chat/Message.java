package com.delgo.reward.domain.chat;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    private String sender;
    private String content;
    private LocalDateTime timestamp;
}
