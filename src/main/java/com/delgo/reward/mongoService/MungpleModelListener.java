package com.delgo.reward.mongoService;

import com.delgo.reward.mongoDomain.MongoMungple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MungpleModelListener extends AbstractMongoEventListener<MongoMungple> {
    private final SequenceGeneratorService sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<MongoMungple> event) {
        event.getSource().setMungpleId((int) sequenceGenerator.generateSequence(MongoMungple.SEQUENCE_NAME));
    }
}