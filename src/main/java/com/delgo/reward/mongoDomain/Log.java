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
import java.util.Map;

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
    @Field("response_map")
    private Map<String, String> responseMap;
    @Field("response_string")
    private String responseStr;
    @Field("created_at")
    private LocalDateTime createdAt;

    public Log toEntity(String httpMethod, String controllerName, String methodName, ArrayMap<String, Object> args, Map<String, String> responseMap){
        return Log.builder()
                .httpMethod(httpMethod)
                .controllerName(controllerName)
                .methodName(methodName)
                .args(args)
                .responseMap(responseMap)
                .createdAt(LocalDateTime.now())
                .build();

    }

    public Log toEntity(String httpMethod, String controllerName, String methodName, ArrayMap<String, Object> args, String responseStr){
        return Log.builder()
                .httpMethod(httpMethod)
                .controllerName(controllerName)
                .methodName(methodName)
                .args(args)
                .responseStr(responseStr)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
