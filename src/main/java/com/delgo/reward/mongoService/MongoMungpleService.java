package com.delgo.reward.mongoService;

import com.delgo.reward.cache.MungpleCache;
import com.delgo.reward.cacheService.MungpleCacheService;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.MungpleSort;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.Bookmark;
import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.dto.mungple.detail.MungpleDetailByMenuResDTO;
import com.delgo.reward.dto.mungple.detail.MungpleDetailByPriceTagResDTO;
import com.delgo.reward.dto.mungple.detail.MungpleDetailResDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
    private final BookmarkService bookmarkService;
    private final ObjectStorageService objectStorageService;

    // Repository
    private final CertRepository certRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MongoMungpleRepository mongoMungpleRepository;


    /**
     * Mungple 생성
     */
    public MongoMungple save(MongoMungple mongoMungple) {
        return mongoMungpleRepository.save(mongoMungple);
    }

    /**
     * Active Mungple 전체 조회
     */
    @Cacheable(cacheNames = MUNGPLE_CACHE_STORE)
    public List<MungpleResDTO> getAllActiveMungple() {
        return mongoMungpleRepository.findByIsActive(true).stream().map(MungpleResDTO::new).toList();
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
     * [categoryCode] Active Mungple 조회 [TODO: Deprecated]
     */
    public List<MungpleResDTO> getActiveMungpleByCategoryCode(CategoryCode categoryCode) {
        List<MongoMungple> mungpleList = !categoryCode.equals(CategoryCode.CA0000)
                ? mongoMungpleRepository.findByCategoryCodeAndIsActive(categoryCode, true)
                : mongoMungpleRepository.findByIsActive(true);

        return mungpleList.stream().map(m ->{
            int certCount = certRepository.countOfCorrectCertByMungple(m.getMungpleId());
            int bookmarkCount = bookmarkService.getActiveBookmarkCount(m.getMungpleId());

            return new MungpleResDTO(m, certCount, bookmarkCount);
        }).toList();
    }

    /**
     * [categoryCode] Active Mungple 조회
     */
    public List<MungpleResDTO> getActiveMungpleByCategoryCode(int userId, CategoryCode categoryCode, MungpleSort sort, String latitude, String longitude) {
        List<MongoMungple> mungpleList = !categoryCode.equals(CategoryCode.CA0000)
                ? mongoMungpleRepository.findByCategoryCodeAndIsActive(categoryCode, true)
                : mongoMungpleRepository.findByIsActive(true);

        // DB Data 조회
        List<MungpleCountDTO> countByBookmark = bookmarkRepository.countBookmarksGroupedByMungpleId();
        List<MungpleCountDTO> countByCert = certRepository.countCertsGroupedByMungpleId();

        // 조건에 맞게 정렬
        MungpleSortingStrategy sortingStrategy = switch (sort) {
            case DISTANCE -> new DistanceSorting(mungpleList, latitude, longitude);
            case BOOKMARK -> new BookmarkCountSorting(mungpleList, countByBookmark.stream().map(MungpleCountDTO::getMungpleId).collect(Collectors.toMap(Function.identity(), Function.identity())));
            case CERT -> new CertCountSorting(mungpleList, countByCert.stream().map(MungpleCountDTO::getMungpleId).collect(Collectors.toMap(Function.identity(), Function.identity())));
            default -> throw new IllegalArgumentException("Unknown sorting type: " + sort);
        };

        List<MungpleResDTO> mungpleResDTOS = convertToMungpleResDTOs(sortingStrategy.sort());
        Set<Integer> bookmarkedMungpleIds = bookmarkRepository.findBookmarkedMungpleIds(userId);

        // IsBookmarked 값 설정
        mungpleResDTOS.forEach(m -> {
            if (bookmarkedMungpleIds.contains(m.getMungpleId())) m.setIsBookmarked(true);
        });

        return mungpleResDTOS;
    }

    /**
     * [BookMark] Active Mungple 조회
     */
    public List<MungpleResDTO> getActiveMungpleByBookMark(int userId, MungpleSort sort, String latitude, String longitude) {
        // 사용자 ID를 기반으로 활성화 된 북마크를 가져온 후 정렬
        List<Bookmark> bookmarkList = bookmarkService.getActiveBookmarkByUserId(userId);
        List<Integer> mungpleIdList = bookmarkList.stream().map(Bookmark::getMungpleId).toList();
        List<MongoMungple> mungpleList = mongoMungpleRepository.findByMungpleIdIn(mungpleIdList);

        // 조건에 맞게 정렬
        MungpleSortingStrategy sortingStrategy = switch (sort) {
            case DISTANCE -> new DistanceSorting(mungpleList, latitude, longitude);
            case NEWEST -> new NewestSorting(mungpleList, bookmarkList);
            case OLDEST -> new OldestSorting(mungpleList, bookmarkList);
            default -> throw new IllegalArgumentException("Unknown sorting type: " + sort);
        };

        // DTO로 변환 (저장 개수, 인증 개수)
        return convertToMungpleResDTOs(sortingStrategy.sort());
    }

    /**
     * MongoMungple -> MungpleResDTO 로 변환
     */
    private List<MungpleResDTO> convertToMungpleResDTOs(List<MongoMungple> mungples) {
        Map<Integer, MungpleCountDTO> bookmarkCountsMap = bookmarkRepository.countBookmarksGroupedByMungpleId().stream().collect(Collectors.toMap(MungpleCountDTO::getMungpleId, Function.identity()));
        Map<Integer, MungpleCountDTO> certCountsMap = certRepository.countCertsGroupedByMungpleId().stream().collect(Collectors.toMap(MungpleCountDTO::getMungpleId, Function.identity()));

        return mungples.stream().map(m -> {
            int bookmarkCount = bookmarkCountsMap.getOrDefault(m.getMungpleId(), new MungpleCountDTO(0, 0)).getCount();
            int certCount = certCountsMap.getOrDefault(m.getMungpleId(), new MungpleCountDTO(0, 0)).getCount();

            return new MungpleResDTO(m, certCount, bookmarkCount, false);
        }).toList();
    }

    /**
     * [User] 가장 많이 방문한 3개의 멍플 조회
     */
    public List<MongoMungple> getTop3VisitMungpleByMungpleIdList(List<Integer> mungpleIdList){
        return mongoMungpleRepository.findByMungpleIdIn(mungpleIdList);
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
     * [PlaceName] Mungple 중복 체크
     */
    public boolean isMungpleExistingByPlaceName(String placeName) {
        return mongoMungpleRepository.existsByPlaceName(placeName);
    }

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

    public List<MongoMungple> findWithin3Km(String latitude, String longitude) {
        double maxDistanceInRadians = 2 / 6371.01;

        Query query = new Query();
        query.addCriteria(Criteria.where("location").withinSphere(new Circle(Double.parseDouble(longitude), Double.parseDouble(latitude), maxDistanceInRadians)));

        return mongoTemplate.find(query, MongoMungple.class);
    }

    public MungpleDetailResDTO getMungpleDetailByMungpleIdAndUserId(int mungpleId, int userId) {
        MongoMungple mongoMungple = getMungpleByMungpleId(mungpleId);
        int certCount = certRepository.countOfCorrectCertByMungple(mungpleId);

        boolean isBookmarked = (userId != 0 && bookmarkService.hasBookmarkByIsBookmarked(userId, mungpleId, true));
        int bookmarkCount = bookmarkService.getActiveBookmarkCount(mungpleId);

        switch (mongoMungple.getCategoryCode()) {
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

