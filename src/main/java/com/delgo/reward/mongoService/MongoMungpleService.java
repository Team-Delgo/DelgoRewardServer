package com.delgo.reward.mongoService;

import com.delgo.reward.cache.MungpleCache;
import com.delgo.reward.cacheService.MungpleCacheService;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.Bookmark;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.dto.mungple.detail.MungpleDetailByMenuResDTO;
import com.delgo.reward.dto.mungple.detail.MungpleDetailByPriceTagResDTO;
import com.delgo.reward.dto.mungple.detail.MungpleDetailResDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoDomain.mungple.MungpleDetail;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.mongoRepository.MungpleDetailRepository;
import com.delgo.reward.record.mungple.MungpleDetailRecord;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MongoMungpleService {
    private final String MUNGPLE_CACHE_STORE = "MungpleCacheStore";

    @Autowired
    private MongoTemplate mongoTemplate;

    // Cache
    private final MungpleCacheService mungpleCacheService;

    // Service
    private final GeoService geoService;
    private final ObjectStorageService objectStorageService;
    private final BookmarkService bookmarkService;

    // Repository
    private final CertRepository certRepository;
    private final MongoMungpleRepository mongoMungpleRepository;
    private final MungpleDetailRepository mungpleDetailRepository;

    /**
     * Mungple 생성
     */

    public MongoMungple save(MongoMungple mongoMungple) {
        return mongoMungpleRepository.save(mongoMungple);
    }

    /**
     * [mungpleId] Mungple 조회
     */
    public MongoMungple getMungpleByMungpleId(int mungpleId) {
        MungpleCache cacheData = mungpleCacheService.getCacheData(mungpleId);

        if (!mungpleCacheService.isValidation(cacheData)) {
            MongoMungple mongoMungple = mongoMungpleRepository.findByMungpleId(mungpleId)
                    .orElseThrow(() -> new NullPointerException("NOT FOUND MongoMungple - mungpleId : " + mungpleId ));
            cacheData = mungpleCacheService.updateCacheData(mungpleId, mongoMungple);
        }

        return cacheData.getMongoMungple();
    }

    /**
     * [categoryCode] Mungple 조회
     */
    @Cacheable(cacheNames = MUNGPLE_CACHE_STORE)
    public List<MungpleResDTO> getMungpleByCategoryCode(String categoryCode) {
        List<MongoMungple> mungpleList = !categoryCode.equals(CategoryCode.CA0000.getCode())
                ? mongoMungpleRepository.findByCategoryCode(categoryCode)
                : mongoMungpleRepository.findAll();

        return mungpleList.stream().map(MungpleResDTO::new).collect(Collectors.toList());
    }

    /**
     * [categoryCode] Active Mungple 조회
     */
    public List<MungpleResDTO> getActiveMungpleByCategoryCode(CategoryCode categoryCode) {
        List<MongoMungple> mungpleList = !categoryCode.equals(CategoryCode.CA0000)
                ? mongoMungpleRepository.findByCategoryCodeAndIsActive(categoryCode, true)
                : mongoMungpleRepository.findByIsActive(true);

        return mungpleList.stream().map(MungpleResDTO::new).collect(Collectors.toList());
    }

    /**
     * [BookMark] Active Mungple 조회
     */
    public List<MungpleResDTO> getActiveMungpleByBookMark(int userId) {
        List<Bookmark> bookmarks = bookmarkService.getBookmarkByUserId(userId);
        List<Integer> mungpleIdList = bookmarks.stream().map(Bookmark::getMungpleId).toList();
        List<MongoMungple> mungpleList = mongoMungpleRepository.findByMungpleIdIn(mungpleIdList);

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

    public List<MungpleResDTO> getMungpleOfMostCertCount(int count) {
        List<MongoMungple> mungpleList = mongoMungpleRepository.findByMungpleIdIn(certRepository.findCertOrderByMungpleCount(PageRequest.of(0, count)));

        return mungpleList.stream().map(MungpleResDTO::new).collect(Collectors.toList());
    }

    /**
     * Mungple Photo 수정
     */
//    public MongoMungple modifyPhoto(int mungpleId, MultipartFile photo){
//         TODO: 고도화 시켜야 함.
//    }

    /**
     * Mungple 삭제
     * NCP Object Storage 도 삭제 해줘야 함.
     * 캐시도 삭제해줘야함
     */
    public void deleteMungple(int mungpleId){
        mongoMungpleRepository.deleteByMungpleId(mungpleId);
        mungpleCacheService.deleteCacheData(mungpleId);
        objectStorageService.deleteObject(BucketName.MUNGPLE,mungpleId + "_mungple.webp"); // Thumbnail delete
        objectStorageService.deleteObject(BucketName.MUNGPLE_NOTE,mungpleId + "_mungplenote.webp"); // mungpleNote delete
    }

    public Boolean isExist(int mungpleId) {
        return mongoMungpleRepository.existsByMungpleId(mungpleId);
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

    public MungpleDetailResDTO getMungpleDetailByMungpleIdAndUserId(int mungpleId, int userId) {
        MongoMungple mongoMungple = getMungpleByMungpleId(mungpleId);
        int certCount = certRepository.countOfCertByMungple(mungpleId);

        boolean isBookmarked = (userId != 0 && bookmarkService.hasBookmarkByIsBookmarked(userId, mungpleId, true));
        int bookmarkCount = bookmarkService.getActiveBookmarkCount(mungpleId);

        switch (CategoryCode.valueOf(mongoMungple.getCategoryCode())) {
            case CA0002, CA0003 -> {
                return new MungpleDetailByMenuResDTO(mongoMungple, certCount, bookmarkCount, isBookmarked);
            }
            default -> {
                return new MungpleDetailByPriceTagResDTO(mongoMungple, certCount, bookmarkCount, isBookmarked);
            }
        }
    }

    public void resetMungpleCache(){
        // Cache 전부 삭제
        mungpleCacheService.deleteAllCacheData();
        // Active Mongo Mungple 조회
        List<MongoMungple> mungpleList = mongoMungpleRepository.findByIsActive(true);
        // Cache 설정
        mungpleList.forEach(m -> mungpleCacheService.updateCacheData(m.getMungpleId(), m));
    }
}

