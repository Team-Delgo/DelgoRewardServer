package com.delgo.reward.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Archive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer archiveId;
    private Integer userId;
    private Integer achievementsId;
    private Integer isMain;

    @Transient
    private Achievements achievements;

    @CreationTimestamp
    private LocalDateTime registDt; // 등록 날짜
}