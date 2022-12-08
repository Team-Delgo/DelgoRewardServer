package com.delgo.reward.domain.achievements;


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

    public Archive setMain(int order){
        this.isMain = order;

        return this;
    }
}