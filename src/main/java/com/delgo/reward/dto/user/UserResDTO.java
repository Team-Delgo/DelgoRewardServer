package com.delgo.reward.dto.user;

import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserResDTO {
    private User user;
    private Pet pet;
}
