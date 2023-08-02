package com.delgo.reward.mongoService;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.mungple.Mungple;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.dto.mungple.detail.MungpleDetailByMenuResDTO;
import com.delgo.reward.dto.mungple.detail.MungpleDetailResDTO;
import com.delgo.reward.dto.mungple.detail.MungpleDetailByPriceTagResDTO;
import com.delgo.reward.mongoDomain.MongoMungple;
import com.delgo.reward.mongoDomain.MungpleDetail;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.mongoRepository.MungpleDetailRepository;
import com.delgo.reward.record.mungple.MungpleDetailRecord;
import com.delgo.reward.record.mungple.MungpleRecord;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.service.MungpleService;
import com.delgo.reward.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MongoMungpleService {

    @Autowired
    private MongoTemplate mongoTemplate;

    // Service
    private final GeoService geoService;
    private final PhotoService photoService;
    private final ObjectStorageService objectStorageService;

    // Repository
    private final CertRepository certRepository;
    private final MongoMungpleRepository mongoMungpleRepository;
    private final MungpleDetailRepository mungpleDetailRepository;

    /**
     * Mungple 생성
     */
    public MungpleResDTO createMungple(MungpleRecord record, MultipartFile photo) {
        Location location = geoService.getGeoData(record.address()); // 위도, 경도

        MongoMungple mongoMungple = mongoMungpleRepository.save(record.toMongoEntity(location));
        mongoMungple.setPhotoUrl(photoService.uploadMongoMungple(mongoMungple.getId(), photo));

        return new MungpleResDTO(mongoMungple);
    }

    /**
     * [mungpleId] Mungple 조회
     */
    public MongoMungple getMungpleById(String mungpleId) {
        return mongoMungpleRepository.findById(mungpleId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND MongoMungple - mungpleId : " + mungpleId ));
    }

    /**
     * [categoryCode] Mungple 조회
     */
    public List<MungpleResDTO> getMungpleByCategoryCode(String categoryCode) {
        List<MongoMungple> mungpleList = !categoryCode.equals(CategoryCode.TOTAL.getCode())
                ? mongoMungpleRepository.findByCategoryCode(categoryCode)
                : mongoMungpleRepository.findAll();

        return mungpleList.stream().map(MungpleResDTO::new).collect(Collectors.toList());
    }

    /**
     * [categoryCode] Active Mungple 조회
     */
    public List<MungpleResDTO> getActiveMungpleByCategoryCode(String categoryCode) {
        List<MongoMungple> mungpleList = !categoryCode.equals(CategoryCode.TOTAL.getCode())
                ? mongoMungpleRepository.findByCategoryCodeAndIsActive(categoryCode, true)
                : mongoMungpleRepository.findByIsActive(true);

        return mungpleList.stream().map(MungpleResDTO::new).collect(Collectors.toList());
    }

    /**
     * [address] Mungple 중복 체크
     * NCP - 위도, 경도 구해야 함.
     */
    public boolean isMungpleExisting(String address) {
        Location location = geoService.getGeoData(address);
        return mongoMungpleRepository.existsByLatitudeAndLongitude(location.getLatitude(), location.getLongitude());
    }

    /**
     * TODO: [인증 개수 많은 순] Mungple 조회
     */
//    public List<MungpleResDTO> getMungpleOfMostCertCount(int count) {
//        List<String> mungpleIds = certRepository.findCertOrderByMungpleCount(PageRequest.of(0, count));
//        List<MongoMungple> mungpleList = mongoMungpleRepository.findMungpleByIds(mungpleIds);
//
//        return mungpleList.stream().map(MungpleResDTO::new).collect(Collectors.toList());
//    }

    /**
     * Mungple Photo 수정
     */
    public MongoMungple modifyPhoto(String mungpleId, MultipartFile photo){
        return getMungpleById(mungpleId).setPhotoUrl(photoService.uploadMongoMungple(mungpleId, photo));
    }

    /**
     * Mungple 삭제
     * NCP Object Storage 도 삭제 해줘야 함.
     */
    public void deleteMungple(String mungpleId){
        mongoMungpleRepository.deleteById(mungpleId);
        objectStorageService.deleteObject(BucketName.MUNGPLE,mungpleId + "_mungple.webp"); // Thumbnail delete
        objectStorageService.deleteObject(BucketName.MUNGPLE_NOTE,mungpleId + "_mungplenote.webp"); // mungpleNote delete
    }

//    public MungpleDetailResDTO getMungpleDetailDataByMungpleId(String mungpleId) {
//        MongoMungple mongoMungple = getMungpleById(mungpleId);
//        MungpleDetail mungpleDetail = mungpleDetailRepository.findByMungpleId(mungpleId).orElseThrow(() -> new NullPointerException("NOT FOUND MUNGPLE: mungpleId = " + mungpleId));
//        int certCount = certRepository.countOfCertByMungple(mungpleId);
//
//        CategoryCode categoryCode = CategoryCode.valueOf(mungple.getCategoryCode());
//        switch (categoryCode){
//            case CA0002, CA0003 -> {
//                return new MungpleDetailByMenuResDTO(mungple, mungpleDetail, certCount);
//            }
//            default -> {
//                return new MungpleDetailByPriceTagResDTO(mungple, mungpleDetail, certCount);
//            }
//        }
//    }

    public Boolean isExist(String mungpleId) {
        return mongoMungpleRepository.existsById(mungpleId);
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

