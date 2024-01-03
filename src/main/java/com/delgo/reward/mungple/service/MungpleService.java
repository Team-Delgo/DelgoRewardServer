package com.delgo.reward.mungple.service;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.MungpleSort;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.comm.ncp.geo.GeoData;
import com.delgo.reward.comm.ncp.geo.GeoDataService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.mungple.repository.MungpleRepository;
import com.delgo.reward.mungple.service.strategy.*;
import com.delgo.reward.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MungpleService {
    private final MungpleRepository mungpleRepository;

    private final GeoDataService geoDataService;
    private final BookmarkService bookmarkService;
    private final ObjectStorageService objectStorageService;


    @Transactional
    public Mungple save(Mungple mungple) {
        return mungpleRepository.save(mungple);
    }

    public List<Mungple> getAll() {
        return mungpleRepository.findListByIsActive(true);
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
         return mungpleRepository.findListByCategoryCodeAndIsActive(categoryCode, true);
    }

    public List<Mungple> getListByIds(List<Integer> mungpleIdList) {
        return mungpleRepository.findListByMungpleIdIn(mungpleIdList);
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

    public MungpleSortingStrategy getSortingStrategy(MungpleSort sort, String latitude, String longitude, int userId) {
        return switch (sort) {
            case DISTANCE -> new DistanceSorting(latitude, longitude);
            case BOOKMARK -> new BookmarkCountSorting(bookmarkService.getCountMapByMungple());
            case CERT -> new CertCountSorting(bookmarkService.getCountMapByMungple());
            case NEWEST -> new NewestSorting(bookmarkService.getActiveBookmarkByUserId(userId));
            case OLDEST -> new OldestSorting(bookmarkService.getActiveBookmarkByUserId(userId));
            case NOT -> new NotSorting();
        };
    }
}

