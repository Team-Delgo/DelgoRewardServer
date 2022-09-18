package com.delgo.reward.dto;

import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserPetDTO {
    private User user;
    private Pet pet;
}
