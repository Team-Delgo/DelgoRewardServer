package com.delgo.reward.user.domain;

import com.delgo.reward.common.domain.BaseTimeEntity;
import com.delgo.reward.user.controller.request.OAuthCreate;
import com.delgo.reward.user.controller.request.PetUpdate;
import com.delgo.reward.user.controller.request.UserCreate;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int petId;
    @Setter
    private String name;
    @Setter
    private String breed; // breedCode
    @Setter
    private String breedName;
    @Setter
    private LocalDate birthday;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

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

    public Pet update(PetUpdate petUpdate, String breedName){
        return Pet.builder()
                .name(petUpdate.name())
                .breed(petUpdate.breed())
                .breedName(breedName)
                .birthday(petUpdate.birthday())
                .user(user)
                .build();
    }
}