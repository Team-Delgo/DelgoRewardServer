package com.delgo.reward.mongoService;

import com.delgo.reward.domain.Mungple;
import com.delgo.reward.dto.mungple.MungpleDetailResDTO;
import com.delgo.reward.mongoDomain.MongoMungple;
import com.delgo.reward.mongoDomain.MungpleDetailData;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.mongoRepository.MungpleDetailDataRepository;
import com.delgo.reward.service.MungpleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoMungpleService {
    @Autowired
    private MongoTemplate mongoTemplate;

    private final MungpleService mungpleService;
    private final MongoMungpleRepository mongoMungpleRepository;
    private final MungpleDetailDataRepository mungpleDetailDataRepository;

    public MungpleDetailResDTO getMungpleDetailDataByMungpleId(int mungpleId) {
        MungpleDetailData mungpleDetailData = mungpleDetailDataRepository.findByMungpleId(mungpleId).orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE: mungpleId = " + mungpleId));
        return new MungpleDetailResDTO(mungpleService.getMungpleById(mungpleId), mungpleDetailData);
    }

//    public void makeMungpletoMongo() {
//        List<Mungple> mungples = mungpleService.getMungpleByMap(CategoryCode.TOTAL.getCode());
//        for(Mungple m : mungples){
//            mongoMungpleRepository.save(m.toMongo());
//        }
//    }

    public List<MongoMungple> findWithin3Km(String latitude, String longitude) {
        double maxDistanceInRadians = 2 / 6371.01;

        Query query = new Query();
        query.addCriteria(Criteria.where("location").withinSphere(new Circle(Double.parseDouble(longitude), Double.parseDouble(latitude), maxDistanceInRadians)));

        return mongoTemplate.find(query, MongoMungple.class);
    }
}

