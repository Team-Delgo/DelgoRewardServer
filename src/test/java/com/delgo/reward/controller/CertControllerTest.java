package com.delgo.reward.controller;

import com.delgo.reward.comm.async.CertAsyncService;
import com.delgo.reward.comm.async.ClassificationAsyncService;
import com.delgo.reward.comm.config.WebConfig;
import com.delgo.reward.comm.security.SecurityConfig;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import com.delgo.reward.dto.cert.CertResDTO;
import com.delgo.reward.mongoService.ClassificationService;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.LikeListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = CertController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
        })
public class CertControllerTest {

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
    @DisplayName("CertificationId로 Certification 조회 테스트")
    void getCertByCertIdTest() throws Exception {
        //given
        int userId = 1;
        int certificationId = 10;
        Pet pet = Pet.builder().petId(12).name("TEST DOG NAME").breed("DT0011").breedName("웰시 코기").birthday(LocalDate.parse("2000-01-01")).build();
        User user = User.builder().userId(userId).email("test@naver.com").name("TEST USER NAME").phoneNo("01012345678").address("서울특별시 송파구").profile("https://kr.object.ncloudstorage.com/reward-profile/276_profile.webp?2302150308309037").geoCode("101180").pGeoCode("101000").isNotify(false).userSocial(UserSocial.K).pet(pet).build();
        Certification certification = Certification.builder().certificationId(certificationId).categoryCode("CA0001").mungpleId(0).placeName("TEST PLACE NAME").description("TEST DESCRIPTION").address("서울특별시 송파구").isHideAddress(false).geoCode("101180").pGeoCode("101000").latitude("37.5022632").longitude("127.1266003").photoUrl("https://kr.object.ncloudstorage.com/reward-certification/938_cert.webp?2302150348341034").isCorrectPhoto(true).isAchievements(true).commentCount(0).isExpose(true).user(user).build();

        // when
        List<CertResDTO> certResDTOS = new ArrayList<>();
        certResDTOS.add(new CertResDTO(certification, user.getUserId()));

        Mockito.when(certService.getCertByUserIdAndCertId(userId, certificationId)).thenReturn(certResDTOS);

        // then
        mockMvc.perform(get("/api/certification")
                        .param("userId", String.valueOf(userId))
                        .param("certificationId", String.valueOf(certificationId)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].certificationId").value(certificationId))
                .andExpect(jsonPath("$.data[0].placeName").value("TEST PLACE NAME"))
                .andExpect(jsonPath("$.data[0].description").value("TEST DESCRIPTION"))
                .andExpect(jsonPath("$.data[0].address").value("서울특별시 송파구"))
                .andExpect(jsonPath("$.data[0].userId").value(userId))
                .andExpect(jsonPath("$.data[0].userName").value("TEST USER NAME"));
    }
}
