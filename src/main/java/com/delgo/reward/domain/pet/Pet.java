package com.delgo.reward.domain.pet;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int petId;
    private String name;
    private String breed;
    private LocalDate birthday;
    private int userId;

    @CreationTimestamp
    @Column(name="regist_dt")
    private LocalDateTime registDt;

    @Transient
    private String breedName;
}