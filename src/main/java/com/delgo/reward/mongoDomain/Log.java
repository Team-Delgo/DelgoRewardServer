package com.delgo.reward.mongoDomain;

import com.google.api.client.util.ArrayMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Field("http_method")
    private String httpMethod;
    @Field("controller_name")
    private String controllerName;
    @Field("method_name")
    private String methodName;
    @Field("args")
    private ArrayMap<String, Object> args;
    @Field("responseDTO")
    private String responseDTO;
    @Field("created_at")
    private LocalDateTime createdAt;

    public Log toEntity(String httpMethod, String controllerName, String methodName, ArrayMap<String, Object> args, String responseDTO){
        return Log.builder()
                .httpMethod(httpMethod)
                .controllerName(controllerName)
                .methodName(methodName)
                .args(args)
                .responseDTO(responseDTO)
                .createdAt(LocalDateTime.now())
                .build();

    }
}
