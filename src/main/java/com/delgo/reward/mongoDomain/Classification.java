package com.delgo.reward.mongoDomain;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="classification")
public class Classification {
    @Id
    private String id;
    @Field("user")
    private User user;
    @Field("pet")
    private Pet pet;
    @Field("certification")
    private Certification certification;
    @Field("category")
    private Map<String, String> category;
    @Field("created_at")
    private LocalDateTime createdAt;

    public Classification toEntity(User user, Pet pet, Certification certification, Map<String, String> category){
        return Classification.builder()
                .user(user)
                .pet(pet)
                .certification(certification)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
