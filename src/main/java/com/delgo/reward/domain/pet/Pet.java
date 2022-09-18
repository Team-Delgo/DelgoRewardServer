package com.delgo.reward.domain.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pet_id")
    private int petId;

    @Column(nullable = false, name="name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name="size")
    private PetSize size;

    @Column(nullable = false, name="birthday")
    private LocalDate birthday;

    @Column(name="user_id")
    private int userId;

    @CreationTimestamp
    @Column(name="regist_dt")
    private LocalDateTime registDt;

}