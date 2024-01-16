package com.delgo.reward.cert.response;

import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.domain.Reaction;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.ReactionCode;
import com.delgo.reward.user.domain.User;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class CertResponseTest {

    @Test
    void from() {
        // given
        User user = User.builder()
                .userId(1)
                .name("테스트 사용자")
                .profile("테스트 프로필")
                .build();
        Certification certification = Certification.builder()
                .certificationId(1)
                .categoryCode(CategoryCode.CA0002)
                .mungpleId(12)
                .placeName("테스트 장소")
                .description("테스트 설명")
                .address("테스트 주소")
                .photos(Arrays.asList("photo1.jpg", "photo2.jpg"))
                .isHideAddress(false)
                .user(user)
                .latitude("37.5665")
                .longitude("126.9780")
                .build();
        List<Reaction> reactionList = List.of(
                Reaction.builder().reactionCode(ReactionCode.CUTE).userId(1).isReaction(true).build(),
                Reaction.builder().reactionCode(ReactionCode.CUTE).userId(2).isReaction(true).build(),
                Reaction.builder().reactionCode(ReactionCode.HELPER).userId(1).isReaction(true).build()
        );

        // when
        CertResponse certResponse = CertResponse.from(user.getUserId(), certification, reactionList);

        // then
        assertThat(certResponse.getUserId()).isEqualTo(user.getUserId());
        assertThat(certResponse.getCategoryCode()).isEqualTo(certification.getCategoryCode());
        assertThat(certResponse.getMungpleId()).isEqualTo(certification.getMungpleId());
        assertThat(certResponse.getPlaceName()).isEqualTo(certification.getPlaceName());
        assertThat(certResponse.getDescription()).isEqualTo(certification.getDescription());
        assertThat(certResponse.getAddress()).isEqualTo(certification.getAddress());
        assertThat(certResponse.getPhotos()).isEqualTo(certification.getPhotos());
        assertThat(certResponse.getIsHideAddress()).isEqualTo(certification.getIsHideAddress());
        assertThat(certResponse.getUserName()).isEqualTo(certification.getUser().getName());
        assertThat(certResponse.getUserProfile()).isEqualTo(certification.getUser().getProfile());
        assertThat(certResponse.getCommentCount()).isEqualTo(certification.getCommentCount());
        assertThat(certResponse.getLatitude()).isEqualTo(certification.getLatitude());
        assertThat(certResponse.getLongitude()).isEqualTo(certification.getLongitude());
        assertThat(certResponse.getRegistDt()).isEqualTo(certification.getRegistDt());
        assertThat(certResponse.getIsOwner()).isEqualTo(certification.getUser().getUserId() == user.getUserId());

        assertThat(certResponse.getReactionCountMap().get(ReactionCode.CUTE)).isEqualTo(2);
        assertThat(certResponse.getReactionCountMap().get(ReactionCode.HELPER)).isEqualTo(1);
        assertThat(certResponse.getReactionMap().get(ReactionCode.HELPER)).isEqualTo(true);
        assertThat(certResponse.getReactionMap().get(ReactionCode.CUTE)).isEqualTo(true);
    }

    @Test
    void fromForClassify() {
        // given
        User user = User.builder()
                .userId(1)
                .name("테스트 사용자")
                .profile("테스트 프로필")
                .build();
        Certification certification = Certification.builder()
                .certificationId(1)
                .categoryCode(CategoryCode.CA0002)
                .mungpleId(12)
                .placeName("테스트 장소")
                .description("테스트 설명")
                .address("테스트 주소")
                .photos(Arrays.asList("photo1.jpg", "photo2.jpg"))
                .isHideAddress(false)
                .user(user)
                .latitude("37.5665")
                .longitude("126.9780")
                .build();

        // when
        CertResponse certResponse = CertResponse.from(certification);

        // then
        assertThat(certResponse.getUserId()).isEqualTo(user.getUserId());
        assertThat(certResponse.getCategoryCode()).isEqualTo(certification.getCategoryCode());
        assertThat(certResponse.getMungpleId()).isEqualTo(certification.getMungpleId());
        assertThat(certResponse.getPlaceName()).isEqualTo(certification.getPlaceName());
        assertThat(certResponse.getDescription()).isEqualTo(certification.getDescription());
        assertThat(certResponse.getAddress()).isEqualTo(certification.getAddress());
        assertThat(certResponse.getPhotos()).isEqualTo(certification.getPhotos());
        assertThat(certResponse.getIsHideAddress()).isEqualTo(certification.getIsHideAddress());
        assertThat(certResponse.getUserName()).isEqualTo(certification.getUser().getName());
        assertThat(certResponse.getUserProfile()).isEqualTo(certification.getUser().getProfile());
        assertThat(certResponse.getCommentCount()).isEqualTo(certification.getCommentCount());
        assertThat(certResponse.getLatitude()).isEqualTo(certification.getLatitude());
        assertThat(certResponse.getLongitude()).isEqualTo(certification.getLongitude());
        assertThat(certResponse.getRegistDt()).isEqualTo(certification.getRegistDt());
    }

    @Test
    void fromList() {
        // given
        User user = User.builder()
                .userId(1)
                .name("테스트 사용자")
                .profile("테스트 프로필")
                .build();

        Certification cert1 = Certification.builder()
                .certificationId(1)
                .categoryCode(CategoryCode.CA0002)
                .mungpleId(12)
                .placeName("테스트 장소 1")
                .description("테스트 설명 1")
                .address("테스트 주소 1")
                .photos(Arrays.asList("photo1.jpg", "photo2.jpg"))
                .isHideAddress(false)
                .user(user)
                .latitude("37.5665")
                .longitude("126.9780")
                .build();

        Certification cert2 = Certification.builder()
                .certificationId(2) // cert2의 ID를 다르게 설정
                .categoryCode(CategoryCode.CA0003) // 다른 카테고리 코드
                .mungpleId(34) // 다른 Mungple ID
                .placeName("테스트 장소 2")
                .description("테스트 설명 2")
                .address("테스트 주소 2")
                .photos(Arrays.asList("photo3.jpg", "photo4.jpg")) // 다른 사진 목록
                .isHideAddress(true) // isHideAddress 값을 다르게 설정
                .user(user) // 동일한 User 객체 사용
                .latitude("37.5711") // 다른 위도
                .longitude("126.9911") // 다른 경도
                .build();


        List<Certification> certList = List.of(cert1, cert2);

        Map<Integer, List<Reaction>> reactionMap = new HashMap<>();
        reactionMap.put(1, Arrays.asList(
                Reaction.builder().reactionCode(ReactionCode.CUTE).userId(1).isReaction(true).build(),
                Reaction.builder().reactionCode(ReactionCode.CUTE).userId(2).isReaction(true).build()
        ));
        reactionMap.put(2, Collections.singletonList(
                Reaction.builder().reactionCode(ReactionCode.HELPER).userId(1).isReaction(true).build()
        ));

        // when
        List<CertResponse> certResponses = CertResponse.fromList(user.getUserId(), certList, reactionMap);

        // then
        assertThat(certResponses).hasSize(2);
        assertThat(certResponses.get(0).getCertificationId()).isEqualTo(cert1.getCertificationId());
        assertThat(certResponses.get(1).getCertificationId()).isEqualTo(cert2.getCertificationId());
    }
}