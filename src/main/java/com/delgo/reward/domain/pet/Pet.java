package com.delgo.reward.domain.pet;

import com.delgo.reward.domain.common.BaseTimeEntity;
import com.delgo.reward.domain.user.User;
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
    private String breedCode;
    private String breedName;
    private LocalDate birthday;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public Pet setName(String name) {
        this.name = name;

        return this;
    }


    public Pet setBreedCode(String breedCode) {
        this.breedCode = breedCode;

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
}