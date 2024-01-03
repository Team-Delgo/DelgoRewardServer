//package com.delgo.reward.service;
//
//import com.delgo.reward.bookmark.service.BookmarkService;
//import com.delgo.reward.bookmark.repository.BookmarkRepository;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.persistence.EntityManager;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class BookmarkServiceTest {
//    @Autowired
//    EntityManager em ;
//
//    @Autowired
//    BookmarkService bookmarkService;
//    @Autowired
//    private BookmarkRepository bookmarkRepository;
//
//    @Test
//    public void getActiveBookmarkSpeedTest(){
//        em.clear();
//
//        bookmarkRepository.findBookmarkedMungpleIds(417);
//        bookmarkRepository.findListByUserId(417);
//    }
//}
