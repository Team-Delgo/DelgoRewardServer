package com.delgo.reward.mongoDomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="test")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Test {
    @Id
    private String id;
    private String content;

    public Test setContent(String content){
        this.content = content;
        return this;
    }
}