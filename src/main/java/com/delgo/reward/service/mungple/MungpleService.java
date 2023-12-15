package com.delgo.reward.service.mungple;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.MungpleSort;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.comm.ncp.geo.GeoData;
import com.delgo.reward.comm.ncp.geo.GeoDataService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.user.Bookmark;
import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import com.delgo.reward.mongoRepository.MungpleRepository;
import com.delgo.reward.service.mungple.strategy.*;
import com.delgo.reward.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MungpleService {
    private final MungpleRepository mungpleRepository;

    private final GeoDataService geoDataService;
    private final BookmarkService bookmarkService;
    private final ObjectStorageService objectStorageService;

    private final CertCountSorting certCountSorting;
    private final BookmarkCountSorting bookmarkCountSorting;

    @Transactional
    public Mungple save(Mungple mungple) {
        return mungpleRepository.save(mungple);
    }

    public Mungple getOneByMungpleId(int mungpleId) {
            return mungpleRepository.findOneByMungpleId(mungpleId)
                    .orElseThrow(() -> new NotFoundDataException("[Mungple] mungpleId : " + mungpleId ));
    }

    public Mungple getOneByPlaceName(String placeName) {
        return mungpleRepository.findOneByPlaceName(placeName)
                    .orElseThrow(() -> new NotFoundDataException("[Mungple] placeName : " + placeName ));
    }

    public List<Mungple> getListByCategoryCode(CategoryCode categoryCode) {
         return !categoryCode.equals(CategoryCode.CA0000)
                ? mungpleRepository.findListByCategoryCodeAndIsActive(categoryCode, true)
                : mungpleRepository.findListByIsActive(true);
    }

    public List<Mungple> getListByBookMark(int userId) {
        // 사용자 ID를 기반으로 활성화 된 북마크를 가져온 후 정렬
        List<Bookmark> bookmarkList = bookmarkService.getActiveBookmarkByUserId(userId);
        List<Integer> mungpleIdList = bookmarkList.stream().map(Bookmark::getMungpleId).toList();
        List<Mungple> mungpleList = mungpleRepository.findListByMungpleIdIn(mungpleIdList);

        return mungpleList;
    }

    public List<UserVisitMungpleCountDTO> getListByIds(List<UserVisitMungpleCountDTO> userVisitMungpleCountDTOList){
        List<Mungple> mungpleList = mungpleRepository.findListByMungpleIdIn(userVisitMungpleCountDTOList.stream().map(UserVisitMungpleCountDTO::getMungpleId).collect(Collectors.toList()));

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

    public boolean isMungpleExisting(String address, String placeName) {
        GeoData geoData = geoDataService.getGeoData(address);
        return mungpleRepository.existsByLatitudeAndLongitude(geoData.getLatitude(), geoData.getLongitude())
                && mungpleRepository.existsByPlaceName(placeName);
    }

    public void delete(int mungpleId){
        mungpleRepository.deleteByMungpleId(mungpleId);
        objectStorageService.deleteObject(BucketName.MUNGPLE,mungpleId + "_mungple.webp"); // Thumbnail delete
        objectStorageService.deleteObject(BucketName.MUNGPLE_NOTE,mungpleId + "_mungplenote.webp"); // mungpleNote delete
    }

    public List<Mungple> sort(List<Mungple> mungpleList, MungpleSort sort, String latitude, String longitude) {
        return switch (sort) {
            case DISTANCE -> new DistanceSorting(latitude, longitude).sort(mungpleList);
            case BOOKMARK -> bookmarkCountSorting.sort(mungpleList);
            case CERT -> certCountSorting.sort(mungpleList);
            case NOT -> mungpleList;
            default -> throw new IllegalArgumentException("Unknown sorting type: " + sort);
        };
    }

    public List<Mungple> sortByBookmark(int userId, List<Mungple> mungpleList, MungpleSort sort, String latitude, String longitude) {
        List<Bookmark> bookmarkList = bookmarkService.getActiveBookmarkByUserId(userId);
        return switch (sort) {
            case DISTANCE -> new DistanceSorting(latitude, longitude).sort(mungpleList);
            case NEWEST -> new NewestSorting(bookmarkList).sort(mungpleList);
            case OLDEST -> new OldestSorting(bookmarkList).sort(mungpleList);
            case NOT -> mungpleList;
            default -> throw new IllegalArgumentException("Unknown sorting type: " + sort);
        };
    }
}

