package com.delgo.reward.cert.domain;

import com.delgo.reward.cert.response.CertResponse;
import com.delgo.reward.user.response.UserResponse;
import lombok.*;
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
    private UserResponse user;
    @Field("certification")
    private CertResponse certification;
    @Field("category")
    private Map<String, String> category;
    @Field("address_sido")
    private String sido;
    @Field("address_sigugun")
    private String sigugun;
    @Field("address_dong")
    private String dong;
    @Field("created_at")
    private LocalDateTime createdAt;

    public static Classification from(Certification certification, Map<String, String> category, String SIDO, String SIGUGUN, String DONG){
        return Classification.builder()
                .user(UserResponse.from(certification.getUser()))
                .certification(CertResponse.from(certification))
                .category(category)
                .sido(SIDO)
                .sigugun(SIGUGUN)
                .dong(DONG)
                .createdAt(LocalDateTime.now().plusHours(9))
                .build();
    }
}
