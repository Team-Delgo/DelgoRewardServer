package com.delgo.reward.user.infrastructure.entity;

import com.delgo.reward.user.domain.Pet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet")
public class PetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer petId;
    private String name;
    private String breed; // breedCode
    private String breedName;
    private LocalDate birthday;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    public Pet toModel(){
        return Pet
                .builder()
                .petId(petId)
                .name(name)
                .breed(breed)
                .breedName(breedName)
                .birthday(birthday)
                .build();
    }

    public static PetEntity from(Pet pet){
        return PetEntity
                .builder()
                .petId(pet.getPetId())
                .name(pet.getName())
                .breed(pet.getBreed())
                .breedName(pet.getBreedName())
                .birthday(pet.getBirthday())
                .build();
    }
}
