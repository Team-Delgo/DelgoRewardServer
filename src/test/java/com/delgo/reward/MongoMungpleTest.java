package com.delgo.reward;


import com.delgo.reward.dto.mungple.MungpleCountDTO;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.repository.BookmarkRepository;
import com.delgo.reward.repository.certification.CertRepository;
import com.delgo.reward.service.strategy.BookmarkCountSorting;
import com.delgo.reward.service.strategy.CertCountSorting;
import com.delgo.reward.service.strategy.MungpleSortingStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoMungpleTest {

    @Autowired
    private MongoMungpleService mongoMungpleService;

    @Autowired
    private MongoMungpleRepository mongoMungpleRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private CertRepository certRepository;

    @Test
    public void deleteAllMongoMunple(){
        mongoMungpleRepository.deleteAll();
    }


    @Test
    public void modifyPhotoUrlsTest() {
        int mungpleId = 2;
        List<String> photos = new ArrayList<>();
        photos.add("https://test.com");


//        mongoMungpleService.modifyPhotoUrls(mungpleId, photos);
    }

    @Test
    public void modifyMenuPhotoUrlsTest() {
        int mungpleId = 2;
        String url = "https:asfsadf";


//        mongoMungpleService.modifyMenuPhotoUrl(mungpleId, url);
    }

    @Test
    public void findWithin3KmTEST() {
        String latitude = "37.4704204";
        String longitude = "127.1412807";

        mongoMungpleService.findWithin3Km(latitude,longitude);
    }

    @Test
    public void BookMarkCountSorting_성공한다() {
        //whne
        List<MongoMungple> mungpleList = mongoMungpleRepository.findByIsActive(true);

        // DB Data 조회
        List<MungpleCountDTO> countByBookmark = bookmarkRepository.countBookmarksGroupedByMungpleId();

        // 조건에 맞게 정렬
        MungpleSortingStrategy sortingStrategy = new BookmarkCountSorting(mungpleList, countByBookmark);
        List<MungpleResDTO> mungpleResDTOS = mongoMungpleService.convertToMungpleResDTOs(sortingStrategy.sort());
        for (MungpleResDTO mungple : mungpleResDTOS) {
            System.out.println("mungple = " + mungple.getPlaceName() + " count : " + mungple.getBookmarkCount());
        }
    }

    @Test
    public void CertCountSorting_성공한다() {
        //whne
        List<MongoMungple> mungpleList = mongoMungpleRepository.findByIsActive(true);

        // DB Data 조회
        List<MungpleCountDTO> countByCert = certRepository.countGroupedByMungpleId();

        // 조건에 맞게 정렬
        MungpleSortingStrategy sortingStrategy = new CertCountSorting(mungpleList, countByCert);
        List<MongoMungple> sortedMungpleList = sortingStrategy.sort();

        List<MungpleResDTO> mungpleResDTOS = mongoMungpleService.convertToMungpleResDTOs(sortedMungpleList);
        for (MungpleResDTO mungple : mungpleResDTOS) {
            System.out.println("mungple = " + mungple.getPlaceName() + " count : " + mungple.getCertCount());
        }

        int count = mungpleResDTOS.size();
        System.out.println("count 1 = " + count);
        System.out.println("count 2 = " + mungpleList.size());
    }
}
