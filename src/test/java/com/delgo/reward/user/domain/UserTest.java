package com.delgo.reward.user.domain;

import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.comm.encoder.CustomPasswordEncoder;
import com.delgo.reward.mock.TestPasswordEncoder;
import com.delgo.reward.token.domain.Token;
import com.delgo.reward.user.controller.request.OAuthCreate;
import com.delgo.reward.user.controller.request.UserCreate;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void fromByDelgo() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .userName("test")
                .email("test@example.com")
                .password("password123")
                .phoneNo("010-1234-5678")
                .geoCode("GEO123")
                .pGeoCode("PGEO123")
                .petName("멍멍이")
                .breed("BREED001")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        String encodedPassword = "123124";
        String phoneNo = "01062511583";
        String address = "경기도 성남시 분당구";
        String version = "1.2.1";


        // when
        User user = User.from(userCreate, encodedPassword, phoneNo, address, version);

        // then
        assertThat(user.getName()).isEqualTo(userCreate.userName());
        assertThat(user.getEmail()).isEqualTo(userCreate.email());
        assertThat(user.getPassword()).isEqualTo(encodedPassword);
        assertThat(user.getPhoneNo()).isEqualTo(phoneNo);
        assertThat(user.getGeoCode()).isEqualTo(userCreate.geoCode());
        assertThat(user.getPGeoCode()).isEqualTo(userCreate.pGeoCode());
        assertThat(user.getAddress()).isEqualTo(address);
        assertThat(user.getVersion()).isEqualTo(version);
        assertThat(user.getRoles()).isEqualTo("ROLE_USER");
        assertThat(user.getViewCount()).isEqualTo(0);
    }

    @Test
    void fromByKakao() {
        // given
        OAuthCreate oAuthCreate = OAuthCreate.builder()
                .userName("test")
                .email("test@example.com")
                .phoneNo("010-1234-5678")
                .geoCode("GEO123")
                .pGeoCode("PGEO123")
                .petName("멍멍이")
                .breed("BREED001")
                .birthday(LocalDate.of(2020, 1, 1))
                .userSocial(UserSocial.K)
                .socialId("kakao Unique No")
                .age(1)
                .gender("M")
                .build();
        String phoneNo = "01062511583";
        String address = "경기도 성남시 분당구";
        String version = "1.2.1";

        // when
        User user = User.from(oAuthCreate, phoneNo, address, version);

        // then
        assertThat(user.getName()).isEqualTo(oAuthCreate.userName());
        assertThat(user.getEmail()).isEqualTo(oAuthCreate.email());
        assertThat(user.getPhoneNo()).isEqualTo(phoneNo);
        assertThat(user.getGeoCode()).isEqualTo(oAuthCreate.geoCode());
        assertThat(user.getPGeoCode()).isEqualTo(oAuthCreate.pGeoCode());
        assertThat(user.getAddress()).isEqualTo(address);
        assertThat(user.getVersion()).isEqualTo(version);
        assertThat(user.getUserSocial()).isEqualTo(oAuthCreate.userSocial());
        assertThat(user.getKakaoId()).isEqualTo(oAuthCreate.socialId());
        assertThat(user.getAge()).isEqualTo(oAuthCreate.age());
        assertThat(user.getGender()).isEqualTo(oAuthCreate.gender());

    }

    @Test
    void fromByApple() {
        // given
        OAuthCreate oAuthCreate = OAuthCreate.builder()
                .userName("test")
                .email("test@example.com")
                .phoneNo("010-1234-5678")
                .geoCode("GEO123")
                .pGeoCode("PGEO123")
                .petName("멍멍이")
                .breed("BREED001")
                .birthday(LocalDate.of(2020, 1, 1))
                .userSocial(UserSocial.A)
                .appleUniqueNo("apple Unique No")
                .build();
        String phoneNo = "01062511583";
        String address = "경기도 성남시 분당구";
        String version = "1.2.1";

        // when
        User user = User.from(oAuthCreate, phoneNo, address, version);

        // then
        assertThat(user.getName()).isEqualTo(oAuthCreate.userName());
        assertThat(user.getPhoneNo()).isEqualTo(phoneNo);
        assertThat(user.getGeoCode()).isEqualTo(oAuthCreate.geoCode());
        assertThat(user.getPGeoCode()).isEqualTo(oAuthCreate.pGeoCode());
        assertThat(user.getAddress()).isEqualTo(address);
        assertThat(user.getVersion()).isEqualTo(version);
        assertThat(user.getUserSocial()).isEqualTo(oAuthCreate.userSocial());
        assertThat(user.getAppleUniqueNo()).isEqualTo(oAuthCreate.appleUniqueNo());
    }

    @Test
    void fromByNaver() {
        // given
        OAuthCreate oAuthCreate = OAuthCreate.builder()
                .userName("test")
                .email("test@example.com")
                .phoneNo("010-1234-5678")
                .geoCode("GEO123")
                .pGeoCode("PGEO123")
                .petName("멍멍이")
                .breed("BREED001")
                .birthday(LocalDate.of(2020, 1, 1))
                .userSocial(UserSocial.N)
                .build();
        String phoneNo = "01062511583";
        String address = "경기도 성남시 분당구";
        String version = "1.2.1";

        // when
        User user = User.from(oAuthCreate, phoneNo, address, version);

        // then
        assertThat(user.getName()).isEqualTo(oAuthCreate.userName());
        assertThat(user.getPhoneNo()).isEqualTo(phoneNo);
        assertThat(user.getGeoCode()).isEqualTo(oAuthCreate.geoCode());
        assertThat(user.getPGeoCode()).isEqualTo(oAuthCreate.pGeoCode());
        assertThat(user.getAddress()).isEqualTo(address);
        assertThat(user.getVersion()).isEqualTo(version);
        assertThat(user.getUserSocial()).isEqualTo(oAuthCreate.userSocial());
    }

    @Test
    void encodePassword() {
        // given
        String password = "123456";
        CustomPasswordEncoder passwordEncoder = new TestPasswordEncoder();

        // when
        String encodedPassword = User.encodePassword(passwordEncoder, password);

        // then
        assertThat(encodedPassword).isEqualTo(passwordEncoder.encode(password));
    }

    @Test
    void formattedPhoneNo() {
        // given
        String phoneNo = "010-6251-1583";

        // when
        String formattedPhoneNo = User.formattedPhoneNo(phoneNo);

        // then
        String expected = "01062511583";
        assertThat(formattedPhoneNo).isEqualTo(expected);
    }

    @Test
    void getFcmToken() {
        // given
        User user = User.builder()
                .token(Token.builder().fcmToken("test token").build())
                .build();

        // when
        String fcmToken = user.getFcmToken();

        // then
        assertThat(fcmToken).isEqualTo("test token");
    }

    @Test
    void getFcmToken_예외처리() {
        // given
        User user = User.builder()
                .token(null)
                .build();

        // when
        String fcmToken = user.getFcmToken();

        // then
        assertThat(fcmToken).isBlank();
    }

    @Test
    void getPetName() {
        // given
        User user = User.builder()
                .pet(Pet.builder().name("test name").build())
                .build();

        // when
        String petName = user.getPetName();

        // then
        assertThat(petName).isEqualTo("test name");
    }

    @Test
    void getPetName_예외처리() {
        // given
        User user = User.builder()
                .pet(null)
                .build();

        // when
        String petName = user.getPetName();

        // then
        assertThat(petName).isBlank();
    }

    @Test
    void setPassword() {
        // given
        User user = new User();
        String password = "test password";

        // when
        user.setPassword(password);

        // then
        assertThat(user.getPassword()).isEqualTo(password);
    }

    @Test
    void setName() {
        // given
        User user = new User();
        String name = "test name";

        // when
        user.setName(name);

        // then
        assertThat(user.getName()).isEqualTo(name);
    }

    @Test
    void setAddress() {
        // given
        User user = new User();
        String address = "test address";

        // when
        user.setAddress(address);

        // then
        assertThat(user.getAddress()).isEqualTo(address);
    }

    @Test
    void setProfile() {
        // given
        User user = new User();
        String profile = "test profile";

        // when
        user.setProfile(profile);

        // then
        assertThat(user.getProfile()).isEqualTo(profile);
    }

    @Test
    void setGeoCode() {
        // given
        User user = new User();
        String geoCode = "test geoCode";

        // when
        user.setGeoCode(geoCode);

        // then
        assertThat(user.getGeoCode()).isEqualTo(geoCode);
    }

    @Test
    void setPGeoCode() {
        // given
        User user = new User();
        String pGeoCode = "test pGeoCode";

        // when
        user.setPGeoCode(pGeoCode);

        // then
        assertThat(user.getPGeoCode()).isEqualTo(pGeoCode);
    }

    @Test
    void setIsNotify() {
        // given
        User user = new User();
        boolean isNotify = true;

        // when
        user.setIsNotify(isNotify);

        // then
        assertThat(user.getIsNotify()).isEqualTo(isNotify);
    }

    @Test
    void setVersion() {
        // given
        User user = new User();
        String version = "1.0.2";

        // when
        user.setVersion(version);

        // then
        assertThat(user.getVersion()).isEqualTo(version);
    }

    @Test
    void setLastAccessDt() {
        // given
        User user = new User();
        LocalDateTime lastAccessDt = LocalDateTime.now();

        // when
        user.setLastAccessDt(lastAccessDt);

        // then
        assertThat(user.getLastAccessDt()).isEqualTo(lastAccessDt);
    }

    @Test
    void setPet() {
        // given
        User user = new User();
        Pet pet = Pet.builder().petId(1).build();

        // when
        user.setPet(pet);

        // then
        assertThat(user.getPet().getPetId()).isEqualTo(pet.getPetId());
    }

    @Test
    void setToken() {
        // given
        User user = new User();
        Token token = Token.builder().userId(1).build();

        // when
        user.setToken(token);

        // then
        assertThat(user.getToken().getUserId()).isEqualTo(token.getUserId());
    }
}