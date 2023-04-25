package com.delgo.reward.mongoService;

import com.delgo.reward.domain.Mungple;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.service.MungpleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoMungpleService {
    private final MongoMungpleRepository mongoMungpleRepository;
    private final MungpleService mungpleService;


    public void makeMungplestoMongo() {
        List<Mungple> mungples = mungpleService.getMungpleAll();
        for(Mungple m : mungples){
            mongoMungpleRepository.save(m.toMongo());
        }
    }
}
