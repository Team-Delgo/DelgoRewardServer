package com.delgo.reward.domain.pet;

import com.delgo.reward.domain.common.BaseTimeEntity;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.record.signup.OAuthSignUpRecord;
import com.delgo.reward.record.signup.SignUpRecord;
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

    public static Pet from(SignUpRecord signUpRecord, User user){
        return Pet.builder()
                .name(signUpRecord.petName())
                .breed(signUpRecord.breed())
                .birthday(signUpRecord.birthday())
                .user(user)
                .build();
    }

    public static Pet from(OAuthSignUpRecord OAuthSignUpRecord, User user){
        return Pet.builder()
                .name(OAuthSignUpRecord.petName())
                .breed(OAuthSignUpRecord.breed())
                .birthday(OAuthSignUpRecord.birthday())
                .user(user)
                .build();
    }
}