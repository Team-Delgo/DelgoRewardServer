package com.delgo.reward.domain.pet;

import com.delgo.reward.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Pet extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int petId;
    private int userId;

    private String name;
    private String breed;
    private LocalDate birthday;
    @Transient
    private String breedName;

    public Pet setBreedName(String breedName){
        this.breedName = breedName;

        return this;
    }
}