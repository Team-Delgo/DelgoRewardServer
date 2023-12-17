package com.delgo.reward.mongoDomain;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.cert.CertResponse;
import com.delgo.reward.dto.user.UserResDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="classification")
public class Classification {
    @Id
    private String id;
    @Field("user")
    private UserResDTO user;
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

    public Classification toEntity(Certification certification, Map<String, String> category, String SIDO, String SIGUGUN, String DONG){
        return Classification.builder()
                .user(new UserResDTO(certification.getUser()))
                .certification(CertResponse.from(certification))
                .category(category)
                .sido(SIDO)
                .sigugun(SIGUGUN)
                .dong(DONG)
                .createdAt(LocalDateTime.now().plusHours(9))
                .build();
    }
}
