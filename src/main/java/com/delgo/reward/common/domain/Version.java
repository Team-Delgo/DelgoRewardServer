package com.delgo.reward.common.domain;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Version {
    @Id
    private String version;
    private boolean isActive; // 활성화 여부

    @CreationTimestamp
    private LocalDateTime registDt;
}