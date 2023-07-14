package com.delgo.reward.controller;

import com.delgo.reward.comm.async.CertAsyncService;
import com.delgo.reward.comm.async.ClassificationAsyncService;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.config.WebConfig;
import com.delgo.reward.comm.security.SecurityConfig;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.cert.CertByAchvResDTO;
import com.delgo.reward.dto.cert.CertResDTO;
import com.delgo.reward.dto.comm.PageResDTO;
import com.delgo.reward.mongoService.ClassificationService;
import com.delgo.reward.record.certification.CertRecord;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.LikeListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = CertController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
        })
public class CertControllerTest {
    @Value("${config.photoDir}")
    String DIR;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @MockBean CertService certService;
    @MockBean LikeListService likeListService;
    @MockBean CertAsyncService certAsyncService;
    @MockBean ClassificationService classificationService;
    @MockBean ClassificationAsyncService classificationAsyncService;

    private Certification certification;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        certification = Certification.builder()
                .certificationId(10)
                .placeName("Test Place")
                .description("Test Description")
                .photoUrl("https://example.com/photo.jpg")
                .mungpleId(0)
                .isHideAddress(false)
                .address("Seoul, South Korea")
                .commentCount(0)
                .user(User.builder()
                        .userId(1)
                        .name("Test User")
                        .profile("https://example.com/profile.jpg")
                        .build())
                .build();
    }

    @Test
    @DisplayName("MockMultipartFile 생성")
    public void getMockMultipartFileTest() throws IOException {
        //given
        String fileName = "testPhoto";
        String contentType = "webp";
        String path = DIR + fileName + "." + contentType;

        // when
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        MockMultipartFile mockMultipartFile = new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);

        // then
        String getFileName = mockMultipartFile.getOriginalFilename();
        String expectedFileName = fileName + "." + contentType;

        Assertions.assertEquals(expectedFileName, getFileName, "파일 이름이 일치하지 않습니다.");
    }

    @Test
    @DisplayName("[API][POST] Cert 생성")
    void createCertTest() throws Exception {
        //given
        CertRecord certRecord = new CertRecord(1, "CA0002", 3, "TEST PLACE", "TEST DESCRIPTION", "", "", true);
        String json = new ObjectMapper().writeValueAsString(certRecord);

        MockMultipartFile data = new MockMultipartFile("data", "data", "application/json", json.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile photo = new MockMultipartFile("photo", "testPhoto.webp", "webp", new FileInputStream(DIR + "testPhoto.webp"));

        int userId = 1;
        int certificationId = 10;

        CertByAchvResDTO resDTO = new CertByAchvResDTO(certification, userId);
        Mockito.when(certService.createCert(certRecord, photo)).thenReturn(resDTO);

        // when & then
        mockMvc.perform(multipart("/api/certification")
                        .file(data)
                        .file(photo))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                // CertByAchvResDTO
                .andExpect(jsonPath("$.data.certificationId").value(certificationId))
                .andExpect(jsonPath("$.data.placeName").value("Test Place"))
                .andExpect(jsonPath("$.data.description").value("Test Description"))
                .andExpect(jsonPath("$.data.photoUrl").value("https://example.com/photo.jpg"))
                .andExpect(jsonPath("$.data.mungpleId").value(0))
                .andExpect(jsonPath("$.data.isHideAddress").value(false))
                .andExpect(jsonPath("$.data.isOwner").value(true))
                .andExpect(jsonPath("$.data.address").value("Seoul, South Korea"))
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.userName").value("Test User"))
                .andExpect(jsonPath("$.data.userProfile").value("https://example.com/profile.jpg"))
                .andExpect(jsonPath("$.data.isLike").value(false))
                .andExpect(jsonPath("$.data.likeCount").value(0))
                .andExpect(jsonPath("$.data.commentCount").value(0))

                .andExpect(jsonPath("$.data.isAchievements").value(false));
    }

    @Test
    @DisplayName("[API][GET] Id로 Cert 조회")
    void getCertByCertIdTest() throws Exception {
        //given
        int userId = 1;
        int certificationId = 10;

        List<CertResDTO> certResDTOS = List.of(new CertResDTO(certification, userId));

        Mockito.when(certService.getCertsByIdWithLike(userId, certificationId)).thenReturn(certResDTOS);

        // when & then
        mockMvc.perform(get("/api/certification")
                        .param("userId", String.valueOf(userId))
                        .param("certificationId", String.valueOf(certificationId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                // CertByAchvResDTO
                .andExpect(jsonPath("$.data[0].certificationId").value(certificationId))
                .andExpect(jsonPath("$.data[0].placeName").value("Test Place"))
                .andExpect(jsonPath("$.data[0].description").value("Test Description"))
                .andExpect(jsonPath("$.data[0].photoUrl").value("https://example.com/photo.jpg"))
                .andExpect(jsonPath("$.data[0].mungpleId").value(0))
                .andExpect(jsonPath("$.data[0].isHideAddress").value(false))
                .andExpect(jsonPath("$.data[0].isOwner").value(true))
                .andExpect(jsonPath("$.data[0].address").value("Seoul, South Korea"))
                .andExpect(jsonPath("$.data[0].userId").value(userId))
                .andExpect(jsonPath("$.data[0].userName").value("Test User"))
                .andExpect(jsonPath("$.data[0].userProfile").value("https://example.com/profile.jpg"))
                .andExpect(jsonPath("$.data[0].isLike").value(false))
                .andExpect(jsonPath("$.data[0].likeCount").value(0))
                .andExpect(jsonPath("$.data[0].commentCount").value(0));
    }

    @Test
    @DisplayName("[API][GET] 전체 인증 조회 - 페이징")
    void getAllCertTest() throws Exception {
        // given
        int userId = 1;
        int certificationId = 10;
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "registDt");

        int size = 0;
        int number = 2;
        boolean isLast = false;

        List<CertResDTO> certResDTOS = List.of(new CertResDTO(certification, userId));
        PageResDTO<CertResDTO> pageResDTO = new PageResDTO<>(certResDTOS, size, number, isLast);

        Mockito.when(certService.getAllCert(userId, pageable)).thenReturn(pageResDTO);

        // when & then
        mockMvc.perform(get("/api/certification/all")
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.content[0].certificationId").value(certificationId))
                .andExpect(jsonPath("$.data.content[0].placeName").value("Test Place"))
                .andExpect(jsonPath("$.data.content[0].description").value("Test Description"))
                .andExpect(jsonPath("$.data.content[0].photoUrl").value("https://example.com/photo.jpg"))
                .andExpect(jsonPath("$.data.content[0].mungpleId").value(0))
                .andExpect(jsonPath("$.data.content[0].isHideAddress").value(false))
                .andExpect(jsonPath("$.data.content[0].isOwner").value(true))
                .andExpect(jsonPath("$.data.content[0].address").value("Seoul, South Korea"))
                .andExpect(jsonPath("$.data.content[0].userId").value(userId))
                .andExpect(jsonPath("$.data.content[0].userName").value("Test User"))
                .andExpect(jsonPath("$.data.content[0].userProfile").value("https://example.com/profile.jpg"))
                .andExpect(jsonPath("$.data.content[0].isLike").value(false))
                .andExpect(jsonPath("$.data.content[0].likeCount").value(0))
                .andExpect(jsonPath("$.data.content[0].commentCount").value(0))

                .andExpect(jsonPath("$.data.size").value(size))
                .andExpect(jsonPath("$.data.number").value(number))
                .andExpect(jsonPath("$.data.last").value(isLast));
    }

    @Test
    @DisplayName("[API][GET] 날짜로 Cert 조회")
    void getCertsByDateTest() throws Exception {
        //given
        String date = "1997-02-04";

        int userId = 1;
        int certificationId = 10;

        List<CertResDTO> certResDTOS = List.of(new CertResDTO(certification, userId));
        Mockito.when(certService.getCertsByDate(userId, LocalDate.parse(date))).thenReturn(certResDTOS);

        // when & then
        mockMvc.perform(get("/api/certification/date")
                        .param("userId", String.valueOf(userId))
                        .param("date", date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                // CertByAchvResDTO
                .andExpect(jsonPath("$.data[0].certificationId").value(certificationId))
                .andExpect(jsonPath("$.data[0].placeName").value("Test Place"))
                .andExpect(jsonPath("$.data[0].description").value("Test Description"))
                .andExpect(jsonPath("$.data[0].photoUrl").value("https://example.com/photo.jpg"))
                .andExpect(jsonPath("$.data[0].mungpleId").value(0))
                .andExpect(jsonPath("$.data[0].isHideAddress").value(false))
                .andExpect(jsonPath("$.data[0].isOwner").value(true))
                .andExpect(jsonPath("$.data[0].address").value("Seoul, South Korea"))
                .andExpect(jsonPath("$.data[0].userId").value(userId))
                .andExpect(jsonPath("$.data[0].userName").value("Test User"))
                .andExpect(jsonPath("$.data[0].userProfile").value("https://example.com/profile.jpg"))
                .andExpect(jsonPath("$.data[0].isLike").value(false))
                .andExpect(jsonPath("$.data[0].likeCount").value(0))
                .andExpect(jsonPath("$.data[0].commentCount").value(0));
    }

    @Test
    @DisplayName("[API][GET] 카테고리별 인증 조회 - 페이징")
    void getCertsByCategoryTest() throws Exception {
        // given
        int userId = 1;
        int certificationId = 10;
        String categoryCode = CategoryCode.CA0006.getCode();
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "registDt");

        int size = 0;
        int number = 2;
        boolean isLast = false;

        List<CertResDTO> certResDTOS = List.of(new CertResDTO(certification, userId));
        PageResDTO<CertResDTO> pageResDTO = new PageResDTO<>(certResDTOS, size, number, isLast);

        Mockito.when(certService.getCertsByCategory(userId, categoryCode, pageable)).thenReturn(pageResDTO);

        // when & then
        mockMvc.perform(get("/api/certification/category")
                        .param("userId", String.valueOf(userId))
                        .param("categoryCode", categoryCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.data.content[0].certificationId").value(certificationId))
                .andExpect(jsonPath("$.data.content[0].placeName").value("Test Place"))
                .andExpect(jsonPath("$.data.content[0].description").value("Test Description"))
                .andExpect(jsonPath("$.data.content[0].photoUrl").value("https://example.com/photo.jpg"))
                .andExpect(jsonPath("$.data.content[0].mungpleId").value(0))
                .andExpect(jsonPath("$.data.content[0].isHideAddress").value(false))
                .andExpect(jsonPath("$.data.content[0].isOwner").value(true))
                .andExpect(jsonPath("$.data.content[0].address").value("Seoul, South Korea"))
                .andExpect(jsonPath("$.data.content[0].userId").value(userId))
                .andExpect(jsonPath("$.data.content[0].userName").value("Test User"))
                .andExpect(jsonPath("$.data.content[0].userProfile").value("https://example.com/profile.jpg"))
                .andExpect(jsonPath("$.data.content[0].isLike").value(false))
                .andExpect(jsonPath("$.data.content[0].likeCount").value(0))
                .andExpect(jsonPath("$.data.content[0].commentCount").value(0))

                .andExpect(jsonPath("$.data.size").value(size))
                .andExpect(jsonPath("$.data.number").value(number))
                .andExpect(jsonPath("$.data.last").value(isLast));
    }

    @Test
    @DisplayName("[API][GET] 최근 인증 조회")
    void getRecentCertsTest() throws Exception {
        // given
        int userId = 1;
        int count = 5;
        int certificationId = 10;

        List<CertResDTO> certResDTOS = List.of(new CertResDTO(certification, userId));
        Mockito.when(certService.getRecentCerts(userId, count)).thenReturn(certResDTOS);

        // when & then
        mockMvc.perform(get("/api/certification/recent")
                        .param("userId", String.valueOf(userId))
                        .param("count", String.valueOf(count)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.data[0].certificationId").value(certificationId))
                .andExpect(jsonPath("$.data[0].placeName").value("Test Place"))
                .andExpect(jsonPath("$.data[0].description").value("Test Description"))
                .andExpect(jsonPath("$.data[0].photoUrl").value("https://example.com/photo.jpg"))
                .andExpect(jsonPath("$.data[0].mungpleId").value(0))
                .andExpect(jsonPath("$.data[0].isHideAddress").value(false))
                .andExpect(jsonPath("$.data[0].isOwner").value(true))
                .andExpect(jsonPath("$.data[0].address").value("Seoul, South Korea"))
                .andExpect(jsonPath("$.data[0].userId").value(userId))
                .andExpect(jsonPath("$.data[0].userName").value("Test User"))
                .andExpect(jsonPath("$.data[0].userProfile").value("https://example.com/profile.jpg"))
                .andExpect(jsonPath("$.data[0].isLike").value(false))
                .andExpect(jsonPath("$.data[0].likeCount").value(0))
                .andExpect(jsonPath("$.data[0].commentCount").value(0))

                .andExpect(jsonPath("$.data.length()").value(certResDTOS.size()));
    }
}
