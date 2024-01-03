package com.delgo.reward.user.domain;

import com.delgo.reward.common.domain.BaseTimeEntity;
import com.delgo.reward.user.controller.request.OAuthCreate;
import com.delgo.reward.user.controller.request.UserCreate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int petId;
    private String name;
    private String breed; // breedCode
    private String breedName;
    private LocalDate birthday;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public Integer getUserId(){
        return this.user.getUserId();
    }

    public Pet setName(String name) {
        this.name = name;

        return this;
    }


    public Pet setBreed(String breed) {
        this.breed = breed;

        return this;
    }

    public Pet setBreedName(String breedName) {
        this.breedName = breedName;

        return this;
    }


    public Pet setBirthday(LocalDate birthday) {
        this.birthday = birthday;

        return this;
    }

    public static Pet from(UserCreate userCreate, User user){
        return Pet.builder()
                .name(userCreate.petName())
                .breed(userCreate.breed())
                .birthday(userCreate.birthday())
                .user(user)
                .build();
    }

    public static Pet from(OAuthCreate OAuthCreate, User user){
        return Pet.builder()
                .name(OAuthCreate.petName())
                .breed(OAuthCreate.breed())
                .birthday(OAuthCreate.birthday())
                .user(user)
                .build();
    }
}