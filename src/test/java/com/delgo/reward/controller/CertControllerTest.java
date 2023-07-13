package com.delgo.reward.controller;

import com.delgo.reward.comm.async.CertAsyncService;
import com.delgo.reward.comm.async.ClassificationAsyncService;
import com.delgo.reward.comm.config.WebConfig;
import com.delgo.reward.comm.security.SecurityConfig;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.cert.CertByAchvResDTO;
import com.delgo.reward.dto.cert.CertResDTO;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
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

        // when
        int userId = 1;
        int certificationId = 10;
        User user = User.builder()
                .userId(userId)
                .name("TEST USER NAME")
                .profile("https://kr.object.ncloudstorage.com/reward-profile/276_profile.webp?2302150308309037")
                .build();
        Certification certification = Certification.builder()
                .certificationId(certificationId)
                .placeName("TEST PLACE NAME")
                .description("TEST DESCRIPTION")
                .photoUrl("https://kr.object.ncloudstorage.com/reward-certification/938_cert.webp?2302150348341034")
                .mungpleId(0)
                .isHideAddress(false)
                .address("서울특별시 송파구")
                .commentCount(0)
                .user(user)
                .build();

        CertByAchvResDTO resDTO = new CertByAchvResDTO(certification, user.getUserId());
        Mockito.when(certService.createCert(certRecord, photo)).thenReturn(resDTO);

        // then
        mockMvc.perform(multipart("/api/certification")
                        .file(data)
                        .file(photo))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                // CertByAchvResDTO
                .andExpect(jsonPath("$.data.certificationId").value(certificationId))
                .andExpect(jsonPath("$.data.placeName").value("TEST PLACE NAME"))
                .andExpect(jsonPath("$.data.description").value("TEST DESCRIPTION"))
                .andExpect(jsonPath("$.data.photoUrl").value("https://kr.object.ncloudstorage.com/reward-certification/938_cert.webp?2302150348341034"))
                .andExpect(jsonPath("$.data.mungpleId").value(0))

                .andExpect(jsonPath("$.data.isHideAddress").value(false))
                .andExpect(jsonPath("$.data.isOwner").value(true))
                .andExpect(jsonPath("$.data.address").value("서울특별시 송파구"))

                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.userName").value("TEST USER NAME"))
                .andExpect(jsonPath("$.data.userProfile").value("https://kr.object.ncloudstorage.com/reward-profile/276_profile.webp?2302150308309037"))

                .andExpect(jsonPath("$.data.isLike").value(false))
                .andExpect(jsonPath("$.data.likeCount").value(0))
                .andExpect(jsonPath("$.data.commentCount").value(0))

                .andExpect(jsonPath("$.data.isAchievements").value(false))
        ;
    }

    @Test
    @DisplayName("[API][GET] Id로 Cert 조회")
    void getCertByCertIdTest() throws Exception {
        //given
        int userId = 1;
        int certificationId = 10;

        User user = User.builder()
                .userId(userId)
                .name("TEST USER NAME")
                .profile("https://kr.object.ncloudstorage.com/reward-profile/276_profile.webp?2302150308309037")
                .build();
        Certification certification = Certification.builder()
                .certificationId(certificationId)
                .placeName("TEST PLACE NAME")
                .description("TEST DESCRIPTION")
                .photoUrl("https://kr.object.ncloudstorage.com/reward-certification/938_cert.webp?2302150348341034")
                .mungpleId(0)
                .isHideAddress(false)
                .address("서울특별시 송파구")
                .commentCount(0)
                .user(user)
                .build();

        // when
        List<CertResDTO> certResDTOS = List.of(new CertResDTO(certification, userId));
        Mockito.when(certService.getCertsByIdWithLike(userId, certificationId)).thenReturn(certResDTOS);

        // then
        mockMvc.perform(get("/api/certification")
                        .param("userId", String.valueOf(userId))
                        .param("certificationId", String.valueOf(certificationId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                // CertByAchvResDTO
                .andExpect(jsonPath("$.data[0].certificationId").value(certificationId))
                .andExpect(jsonPath("$.data[0].placeName").value("TEST PLACE NAME"))
                .andExpect(jsonPath("$.data[0].description").value("TEST DESCRIPTION"))
                .andExpect(jsonPath("$.data[0].photoUrl").value("https://kr.object.ncloudstorage.com/reward-certification/938_cert.webp?2302150348341034"))
                .andExpect(jsonPath("$.data[0].mungpleId").value(0))

                .andExpect(jsonPath("$.data[0].isHideAddress").value(false))
                .andExpect(jsonPath("$.data[0].isOwner").value(true))
                .andExpect(jsonPath("$.data[0].address").value("서울특별시 송파구"))

                .andExpect(jsonPath("$.data[0].userId").value(userId))
                .andExpect(jsonPath("$.data[0].userName").value("TEST USER NAME"))
                .andExpect(jsonPath("$.data[0].userProfile").value("https://kr.object.ncloudstorage.com/reward-profile/276_profile.webp?2302150308309037"))

                .andExpect(jsonPath("$.data[0].isLike").value(false))
                .andExpect(jsonPath("$.data[0].likeCount").value(0))
                .andExpect(jsonPath("$.data[0].commentCount").value(0));
    }
}
