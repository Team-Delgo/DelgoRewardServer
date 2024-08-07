package com.delgo.reward.mungple.service;

import com.delgo.reward.mungple.domain.Mungple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MungpleModelListener extends AbstractMongoEventListener<Mungple> {
    private final SequenceGeneratorService sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Mungple> event) {
        if(event.getSource().getId() == null){
            event.getSource().setMungpleId((int) sequenceGenerator.generateSequence(Mungple.SEQUENCE_NAME));
        }
    }
}