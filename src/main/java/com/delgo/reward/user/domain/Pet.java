package com.delgo.reward.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class Pet {
    private Integer petId;
    private String name;
    private String breed; // breedCode
    private String breedName;
    private LocalDate birthday;

    private User user;

    public Integer getUserId(){
        return this.user.getUserId();
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
