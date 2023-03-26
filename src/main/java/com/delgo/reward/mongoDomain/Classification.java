package com.delgo.reward.mongoDomain;

import com.delgo.reward.comm.code.CategoryCode;
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
    @Field("category_code")
    private String categoryCode;
    @Field("category_name")
    private String categoryName;
    @Field("created_at")
    private LocalDateTime createdAt;
}
