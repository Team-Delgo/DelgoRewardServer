package com.delgo.reward.controller;

import com.delgo.reward.comm.fcm.FcmService;
import com.delgo.reward.comm.security.SecurityConfig;
import com.delgo.reward.domain.Comment;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.comment.CommentResDTO;
import com.delgo.reward.record.comment.CommentRecord;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.CommentService;
import com.delgo.reward.service.NotifyService;
import com.delgo.reward.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = CommentController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
        })
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @MockBean private CommentService commentService;
    @MockBean private FcmService fcmService;
    @MockBean private CertService certService;
    @MockBean private UserService userService;
    @MockBean private NotifyService notifyService;


    private User user;
    private Certification certification;
    private Comment comment;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        user = User.builder()
                .userId(1)
                .name("Test User")
                .profile("https://example.com/profile.jpg")
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
                .latitude("37.5101562")
                .longitude("127.1091707")
                .user(user)
                .build();
        comment = Comment.builder()
                .commentId(1)
                .certificationId(10)
                .user(user)
                .content("Test Comment")
                .parentCommentId(null)
                .isReply(false)
                .build();
    }

    int userId = 1;
    int certificationId = 10;
    int commentId = 1;

    @Test
    @DisplayName("[API][POST] Comment 생성")
    void createCommentTest() throws Exception {
        CommentRecord commentRecord = new CommentRecord(comment.getUser().getUserId(), comment.getCertificationId(), comment.getContent());

        CommentResDTO resDTO = new CommentResDTO(comment);
        Mockito.when(commentService.createComment(commentRecord)).thenReturn(resDTO);

        mockMvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(commentRecord)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.data.commentId").value(commentId))
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.userName").value("Test User"))
                .andExpect(jsonPath("$.data.userProfile").value("https://example.com/profile.jpg"))
                .andExpect(jsonPath("$.data.certificationId").value(certificationId))
                .andExpect(jsonPath("$.data.isReply").value(false))
                .andExpect(jsonPath("$.data.content").value("Test Comment"));


    }


}
