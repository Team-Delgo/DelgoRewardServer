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
@Document(collection="log")
public class Log {
    @Id
    private String id;
    private String httpMethod;
    private String controllerName;
    private String methodName;
    private ArrayMap<String, Object> args;
    private String responseDTO;
    private LocalDateTime createAt;

    public Log toEntity(String httpMethod, String controllerName, String methodName, ArrayMap<String, Object> args, String responseDTO){
        return Log.builder()
                .httpMethod(httpMethod)
                .controllerName(controllerName)
                .methodName(methodName)
                .args(args)
                .responseDTO(responseDTO)
                .createAt(LocalDateTime.now())
                .build();

    }
}
