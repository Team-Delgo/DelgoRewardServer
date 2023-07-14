package com.delgo.reward.mongoService;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.mungple.Mungple;
import com.delgo.reward.dto.mungple.detail.MungpleDetailByMenuResDTO;
import com.delgo.reward.dto.mungple.detail.MungpleDetailResDTO;
import com.delgo.reward.dto.mungple.detail.MungpleDetailByPriceTagResDTO;
import com.delgo.reward.mongoDomain.MongoMungple;
import com.delgo.reward.mongoDomain.MungpleDetail;
import com.delgo.reward.mongoRepository.MungpleDetailRepository;
import com.delgo.reward.record.mungple.MungpleDetailRecord;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.service.MungpleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MongoMungpleService {

    @Autowired
    private MongoTemplate mongoTemplate;

    // Service
    private final MungpleService mungpleService;

    // Repository
    private final CertRepository certRepository;
    private final MungpleDetailRepository mungpleDetailRepository;

    public MungpleDetailResDTO getMungpleDetailDataByMungpleId(int mungpleId) {
        Mungple mungple = mungpleService.getMungpleById(mungpleId);
        MungpleDetail mungpleDetail = mungpleDetailRepository.findByMungpleId(mungpleId).orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE: mungpleId = " + mungpleId));
        int certCount = certRepository.countOfCertByMungple(mungpleId);

        CategoryCode categoryCode = CategoryCode.valueOf(mungple.getCategoryCode());
        switch (categoryCode){
            case CA0002, CA0003 -> {
                return new MungpleDetailByMenuResDTO(mungple, mungpleDetail, certCount);
            }
            default -> {
                return new MungpleDetailByPriceTagResDTO(mungple, mungpleDetail, certCount);
            }
        }
    }

    public Boolean isExist(int mungpleId) {
        return mungpleDetailRepository.existsByMungpleId(mungpleId);
    }

    public MungpleDetail createMungpleDetail(MungpleDetailRecord record) {
        return mungpleDetailRepository.save(record.makeDetailData());
    }

    public List<MongoMungple> findWithin3Km(String latitude, String longitude) {
        double maxDistanceInRadians = 2 / 6371.01;

        Query query = new Query();
        query.addCriteria(Criteria.where("location").withinSphere(new Circle(Double.parseDouble(longitude), Double.parseDouble(latitude), maxDistanceInRadians)));

        return mongoTemplate.find(query, MongoMungple.class);
    }
}

