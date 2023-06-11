package com.delgo.reward.mongoService;

import com.delgo.reward.domain.Mungple;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.dto.mungple.MungpleDetailResDTO;
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
    private final ObjectStorageService objectStorageService;

    // Repository
    private final CertRepository certRepository;
    private final MungpleDetailRepository mungpleDetailRepository;

    public MungpleDetailResDTO getMungpleDetailDataByMungpleId(int mungpleId) {
        MungpleDetail mungpleDetail = mungpleDetailRepository.findByMungpleId(mungpleId).orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE: mungpleId = " + mungpleId));
        int certCount = certRepository.countOfCertByMungple(mungpleId);

        return new MungpleDetailResDTO(mungpleService.getMungpleById(mungpleId), mungpleDetail, certCount);
    }

    public Boolean isExist(int mungpleId) {
        return mungpleDetailRepository.existsByMungpleId(mungpleId);
    }

    public MungpleDetail createMungpleDetail(MungpleDetailRecord record) {
        return mungpleDetailRepository.save(record.makeDetailData());
    }

    public void addMenuByNCP(int mungpleId) {
        Mungple mungple = mungpleService.getMungpleById(mungpleId);

        List<String> thumbnails = objectStorageService.selectMungpleDetailObjects("reward-detail-thumbnail", mungple.getPlaceName());
        String ncp = "https://kr.object.ncloudstorage.com/reward-detail-thumbnail/";
        List<String> url = thumbnails.stream().map(u -> ncp + u).toList();

        MungpleDetail mungpleDetail = mungpleDetailRepository.findByMungpleId(mungpleId).orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE: mungpleId = " + mungpleId));
        if (mungpleDetail.getPhotoUrls() != null)
            mungpleDetail.getPhotoUrls().clear(); // 초기화

        mungpleDetail.setPhotoUrls(url);

        mungpleDetailRepository.save(mungpleDetail);
    }

    public void addPhotoUrlsByNCP(int mungpleId) {
        Mungple mungple = mungpleService.getMungpleById(mungpleId);

        List<String> thumbnails = objectStorageService.selectMungpleDetailObjects("reward-detail-thumbnail", mungple.getPlaceName());
        String ncp = "https://kr.object.ncloudstorage.com/reward-detail-thumbnail/";
        List<String> url = thumbnails.stream().map(u -> ncp + u).toList();

        MungpleDetail mungpleDetail = mungpleDetailRepository.findByMungpleId(mungpleId).orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE: mungpleId = " + mungpleId));
        if (mungpleDetail.getPhotoUrls() != null)
            mungpleDetail.getPhotoUrls().clear(); // 초기화

        mungpleDetail.setPhotoUrls(url);

        mungpleDetailRepository.save(mungpleDetail);
    }

    public void addPhotoUrls(int mungpleId, List<String> photoUrls) {
        MungpleDetail mungpleDetail = mungpleDetailRepository.findByMungpleId(mungpleId).orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE: mungpleId = " + mungpleId));
        if (mungpleDetail.getPhotoUrls() != null)
            mungpleDetail.getPhotoUrls().clear(); // 초기화

        mungpleDetail.setPhotoUrls(photoUrls);

        mungpleDetailRepository.save(mungpleDetail);
    }

    public void addMenuBoardPhotoUrl(int mungpleId, List<String> menuPhotos) {
        MungpleDetail mungpleDetail = mungpleDetailRepository.findByMungpleId(mungpleId).orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE: mungpleId = " + mungpleId));
        if (mungpleDetail.getRepresentMenuPhotoUrls() != null)
            mungpleDetail.getRepresentMenuPhotoUrls().clear();

        mungpleDetail.setRepresentMenuPhotoUrls(menuPhotos);
        mungpleDetailRepository.save(mungpleDetail);
    }

    public void modifyEnterDesc(int mungpleId, String desc) {
        MungpleDetail mungpleDetail = mungpleDetailRepository.findByMungpleId(mungpleId).orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE: mungpleId = " + mungpleId));
        mungpleDetail.setEnterDesc(desc);

        mungpleDetailRepository.save(mungpleDetail);
    }

    public List<MongoMungple> findWithin3Km(String latitude, String longitude) {
        double maxDistanceInRadians = 2 / 6371.01;

        Query query = new Query();
        query.addCriteria(Criteria.where("location").withinSphere(new Circle(Double.parseDouble(longitude), Double.parseDouble(latitude), maxDistanceInRadians)));

        return mongoTemplate.find(query, MongoMungple.class);
    }
}

