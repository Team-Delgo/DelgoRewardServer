package com.delgo.reward.mongoService;

import com.delgo.reward.cache.MungpleCache;
import com.delgo.reward.cacheService.MungpleCacheService;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.MungpleSort;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.comm.ncp.geo.GeoData;
import com.delgo.reward.comm.ncp.geo.GeoDataService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.user.Bookmark;
import com.delgo.reward.dto.mungple.detail.MungpleDetailByMenuResponse;
import com.delgo.reward.dto.mungple.detail.MungpleDetailByPriceTagResponse;
import com.delgo.reward.dto.mungple.detail.MungpleDetailResponse;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import com.delgo.reward.mongoRepository.MungpleRepository;
import com.delgo.reward.repository.BookmarkRepository;
import com.delgo.reward.service.strategy.*;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MungpleService {
    private final String MUNGPLE_CACHE_STORE = "MungpleCacheStore";

    @Autowired
    private MongoTemplate mongoTemplate;

    // Cache
    private final MungpleCacheService mungpleCacheService;

    // Service
    private final GeoDataService geoDataService;
    private final BookmarkService bookmarkService;
    private final ObjectStorageService objectStorageService;

    // Repository
    private final CertRepository certRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MungpleRepository mungpleRepository;

    private final BookmarkCountSorting bookmarkCountSorting;
    private final CertCountSorting certCountSorting;

    /**
     * Mungple 생성
     */
    public Mungple save(Mungple mungple) {
        return mungpleRepository.save(mungple);
    }

    /**
     * Active Mungple 전체 조회
     */
    @Cacheable(cacheNames = MUNGPLE_CACHE_STORE)
    public List<Mungple> getAllActiveMungple() {
        return mungpleRepository.findByIsActive(true);
    }

    /**
     * [mungpleId] Mungple 조회
     */
    public Mungple getMungpleByMungpleId(int mungpleId) {
        MungpleCache cacheData = mungpleCacheService.getCacheData(mungpleId);

        if (!mungpleCacheService.isValidation(cacheData)) {
            Mungple mungple = mungpleRepository.findByMungpleId(mungpleId)
                    .orElseThrow(() -> new NotFoundDataException("[Mungple] mungpleId : " + mungpleId ));
            cacheData = mungpleCacheService.updateCacheData(mungpleId, mungple);
        }

        return cacheData.getMungple();
    }

    /**
     * [mungpleId] Mungple 조회
     */
    public Mungple getByPlaceName(String placeName) {
        return mungpleRepository.findByPlaceName(placeName)
                    .orElseThrow(() -> new NotFoundDataException("[Mungple] placeName : " + placeName ));
    }

    /**
     * [categoryCode] Active Mungple 조회 [TODO: Deprecated]
     */
    public List<Mungple> getActiveMungpleByCategoryCode(CategoryCode categoryCode) {
         return !categoryCode.equals(CategoryCode.CA0000)
                ? mungpleRepository.findByCategoryCodeAndIsActive(categoryCode, true)
                : mungpleRepository.findByIsActive(true);
    }

    public List<Mungple> sort(List<Mungple> mungpleList, MungpleSort sort, String latitude, String longitude) {
        MungpleSortingStrategy sortingStrategy = switch (sort) {
            case DISTANCE -> new DistanceSorting(latitude, longitude);
            case BOOKMARK -> bookmarkCountSorting;
            case CERT -> certCountSorting;
            default -> throw new IllegalArgumentException("Unknown sorting type: " + sort);
        };

        return sortingStrategy.sort(mungpleList);
    }

    public List<Mungple> sortByBookmark(int userId, List<Mungple> mungpleList, MungpleSort sort, String latitude, String longitude) {
        List<Bookmark> bookmarkList = bookmarkService.getActiveBookmarkByUserId(userId);
        MungpleSortingStrategy sortingStrategy = switch (sort) {
            case DISTANCE -> new DistanceSorting(latitude, longitude);
            case NEWEST -> new NewestSorting(bookmarkList);
            case OLDEST -> new OldestSorting(bookmarkList);
            default -> throw new IllegalArgumentException("Unknown sorting type: " + sort);
        };

        return sortingStrategy.sort(mungpleList);
    }

    /**
     * [BookMark] Active Mungple 조회
     */
    public List<Mungple> getActiveMungpleByBookMark(int userId) {
        // 사용자 ID를 기반으로 활성화 된 북마크를 가져온 후 정렬
        List<Bookmark> bookmarkList = bookmarkService.getActiveBookmarkByUserId(userId);
        List<Integer> mungpleIdList = bookmarkList.stream().map(Bookmark::getMungpleId).toList();
        List<Mungple> mungpleList = mungpleRepository.findByMungpleIdIn(mungpleIdList);

        return mungpleList;
    }

    /**
     * [User] 멍플 아이디 리스트로 멍플 리스트 조회 후 카운트와 함께 반환
     */
    public List<UserVisitMungpleCountDTO> getMungpleListByIds(List<UserVisitMungpleCountDTO> userVisitMungpleCountDTOList){
        List<Mungple> mungpleList = mungpleRepository.findByMungpleIdIn(userVisitMungpleCountDTOList.stream().map(UserVisitMungpleCountDTO::getMungpleId).collect(Collectors.toList()));

        for(Mungple mungple : mungpleList){
            userVisitMungpleCountDTOList.replaceAll(e -> {
                if(Objects.equals(e.getMungpleId(), mungple.getMungpleId())){
                    return e.setMungpleData(mungple.getPlaceName(), mungple.getPhotoUrls().get(0));
                } else {
                    return e;
                }
            });
        }
        return userVisitMungpleCountDTOList;
    }

    /**
     * [address] Mungple 중복 체크
     * NCP - 위도, 경도 구해야 함.
     */
    public boolean isMungpleExisting(String address, String placeName) {
        GeoData geoData = geoDataService.getGeoData(address);
        return mungpleRepository.existsByLatitudeAndLongitude(geoData.getLatitude(), geoData.getLongitude())
                && mungpleRepository.existsByPlaceName(placeName);
    }

    /**
     * Mungple 삭제
     * NCP Object Storage 도 삭제 해줘야 함.
     * 캐시도 삭제해줘야함
     */
    public void deleteMungple(int mungpleId){
        mungpleRepository.deleteByMungpleId(mungpleId);
        mungpleCacheService.deleteCacheData(mungpleId);
        objectStorageService.deleteObject(BucketName.MUNGPLE,mungpleId + "_mungple.webp"); // Thumbnail delete
        objectStorageService.deleteObject(BucketName.MUNGPLE_NOTE,mungpleId + "_mungplenote.webp"); // mungpleNote delete
    }

    public List<Mungple> findWithin3Km(String latitude, String longitude) {
        double maxDistanceInRadians = 2 / 6371.01;

        Query query = new Query();
        query.addCriteria(Criteria.where("location").withinSphere(new Circle(Double.parseDouble(longitude), Double.parseDouble(latitude), maxDistanceInRadians)));

        return mongoTemplate.find(query, Mungple.class);
    }

    public MungpleDetailResponse getMungpleDetailByMungpleIdAndUserId(int mungpleId, int userId) {
        Mungple mungple = getMungpleByMungpleId(mungpleId);
        int certCount = certRepository.countOfCorrectCertByMungple(mungpleId);

        boolean isBookmarked = (userId != 0 && bookmarkService.hasBookmarkByIsBookmarked(userId, mungpleId, true));
        int bookmarkCount = bookmarkService.getActiveBookmarkCount(mungpleId);

        switch (mungple.getCategoryCode()) {
            case CA0002, CA0003 -> {
                return new MungpleDetailByMenuResponse(mungple, certCount, bookmarkCount, isBookmarked);
            }
            default -> {
                return new MungpleDetailByPriceTagResponse(mungple, certCount, bookmarkCount, isBookmarked);
            }
        }
    }

    public void resetMungpleCache(){
        // Cache 전부 삭제
        mungpleCacheService.deleteAllCacheData();
        // Active Mongo Mungple 조회
        List<Mungple> mungpleList = mungpleRepository.findByIsActive(true);
        // Cache 설정
        mungpleList.forEach(m -> mungpleCacheService.updateCacheData(m.getMungpleId(), m));
    }
}

