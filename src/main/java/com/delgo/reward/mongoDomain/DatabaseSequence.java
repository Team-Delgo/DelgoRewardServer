package com.delgo.reward.mongoDomain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Getter
@Setter
@Document(collection = "database_sequences")
public class DatabaseSequence {
    @Id
    private String id;
    private Integer seq;
}