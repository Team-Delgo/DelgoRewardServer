package com.delgo.reward.mongoDomain;

import com.google.api.client.util.ArrayMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="managing")
public class Managing {
    @Id
    private String id;
    private String controllerName;
    private String methodName;
    private ArrayMap<String, Object> args;
    private LocalDateTime createAt;

    public Managing toEntity(String controllerName, String methodName, ArrayMap<String, Object> args){
        return Managing.builder()
                .controllerName(controllerName)
                .methodName(methodName)
                .args(args)
                .createAt(LocalDateTime.now())
                .build();

    }
}
