package com.delgo.reward.service.user;


import com.delgo.reward.cache.ActivityCache;
import com.delgo.reward.cacheService.ActivityCacheService;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.oauth.KakaoService;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoRepository.ClassificationRepository;
import com.delgo.reward.record.signup.OAuthSignUpRecord;
import com.delgo.reward.record.signup.SignUpRecord;
import com.delgo.reward.record.user.ModifyUserRecord;
import com.delgo.reward.repository.JDBCTemplatePointRepository;
import com.delgo.reward.repository.JDBCTemplateRankingRepository;
import com.delgo.reward.repository.UserRepository;
import com.delgo.reward.service.CodeService;
import com.delgo.reward.service.PetService;
import com.delgo.reward.service.PhotoService;
import com.delgo.reward.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {
    private final PasswordEncoder passwordEncoder;

    // Service
    private final PetService petService;
    private final UserQueryService userQueryService;
    private final CodeService codeService;
    private final TokenService tokenService;
    private final KakaoService kakaoService;
    private final PhotoService photoService;

    // Repository
    private final UserRepository userRepository;
    private final ClassificationRepository classificationRepository;

    // Cache
    private final ActivityCacheService activityCacheService;

    // JDBC Templates
    private final JDBCTemplatePointRepository jdbcTemplatePointRepository;
    private final JDBCTemplateRankingRepository jdbcTemplateRankingRepository;

    // 기본 프로필
    private final String DEFAULT_PROFILE = "https://kr.object.ncloudstorage.com/reward-profile/%EA%B8%B0%EB%B3%B8%ED%94%84%EB%A1%9C%ED%95%84.webp";

    // DB 저장
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * 회원가입
     *
     * @param signUpRecord
     * @param profile
     * @return 가입한 회원 정보 반환
     */
    @Transactional
    public User signup(SignUpRecord signUpRecord, MultipartFile profile, String version) {
        // 주소 설정
        String address = codeService.getAddressByGeoCode(signUpRecord.geoCode());

        // USER & PET 저장
        User user = save(signUpRecord.makeUser(passwordEncoder.encode(signUpRecord.password()), address));
        Pet pet = petService.register(signUpRecord.makePet(user));

        jdbcTemplatePointRepository.createUserPoint(user); // Point 생성

        user.setVersion(version);
        String profileUrl = DEFAULT_PROFILE;
        if(!profile.isEmpty()){
            String fileName = photoService.makeProfileFileName(user.getUserId(), profile);
            profileUrl = photoService.saveAndUpload(fileName, profile, BucketName.PROFILE);
        }

        user.setPet(pet);
        user.setProfile(profileUrl);

        return user;
    }

    /**
     * OAuth 회원가입
     *
     * @param oAuthSignUpRecord
     * @param profile
     * @return 가입한 회원 정보 반환
     */
    public User oAuthSignup(OAuthSignUpRecord oAuthSignUpRecord, MultipartFile profile, String version) {
        // 주소 설정
        String address = codeService.getAddressByGeoCode(oAuthSignUpRecord.geoCode());

        // USER & PET 저장
        User oAuthUser = save(oAuthSignUpRecord.makeUser(oAuthSignUpRecord.userSocial(), address));
        Pet pet = petService.register(oAuthSignUpRecord.makePet(oAuthUser));

        jdbcTemplatePointRepository.createUserPoint(oAuthUser); // Point 생성

//        rankingService.rankingByPoint(); // 랭킹 업데이트
        oAuthUser.setVersion(version);

        String profileUrl = DEFAULT_PROFILE;
        if(!profile.isEmpty()){
            String fileName = photoService.makeProfileFileName(oAuthUser.getUserId(), profile);
            profileUrl = photoService.saveAndUpload(fileName, profile, BucketName.PROFILE);
        }

        oAuthUser.setPet(pet);
        oAuthUser.setProfile(profileUrl);

        return oAuthUser;
    }

    /**
     * 회원 탈퇴
     */
    public void delete(int userId) {
        userRepository.deleteByUserId(userId);
    }

    /**
     * 로그아웃
     *
     * @param userId
     * @throws Exception
     */
    public void logout(int userId) throws Exception {
        User user = userQueryService.getOneByUserId(userId);
        if (user.getUserSocial().equals(UserSocial.K))
            kakaoService.logout(user.getKakaoId()); // kakao 로그아웃 , Naver는 로그아웃 지원 X

        tokenService.deleteToken(userId);
    }

    /**
     * 비밀번호 변경
     *
     * @param checkedEmail
     * @param newPassword
     */
    public void changePassword(String checkedEmail, String newPassword) {
        User user = userQueryService.getOneByEmail(checkedEmail);
        user.setPassword(passwordEncoder.encode(newPassword));
    }




    /**
     * 알림 동의 여부 수정
     *
     * @param userId
     * @return 수정된 알람 동의 여부 반환
     */
    public boolean changeNotify(int userId) {
        return userQueryService.getOneByUserId(userId).setNotify();
    }


    public User changePhoto(int userId, String ncpLink) {
        return userQueryService.getOneByUserId(userId).setProfile(ncpLink);
    }

    /**
     * 유저 정보 수정
     *
     * @param modifyUserRecord
     * @return 수정된 유저 정보 반환
     */
    public User changeUserInfo(ModifyUserRecord modifyUserRecord) {
        User user = userQueryService.getOneByUserId(modifyUserRecord.userId());

        if (modifyUserRecord.geoCode() != null && modifyUserRecord.pGeoCode() != null) {
            user.setGeoCode(modifyUserRecord.geoCode());
            user.setPGeoCode(modifyUserRecord.pGeoCode());

            // 주소 설정
            String address = codeService.getAddressByGeoCode(modifyUserRecord.geoCode());
            user.setAddress(address);

            jdbcTemplatePointRepository.changeGeoCode(user.getUserId(), modifyUserRecord.geoCode());
        }

        Optional.ofNullable(modifyUserRecord.name()).ifPresent(user::setName);
        return user;
    }


    public void increaseViewCount(int userId) {
        userRepository.increaseViewCount(userId);
    }

    public Map<CategoryCode, Integer> getActivityByUserId(Integer userId) {
        ActivityCache activityCache = activityCacheService.getCacheData(userId);

        if (!activityCacheService.isValidation(activityCache)) {
            activityCache = makeActivityCacheValue(userId);
        }

        Map<CategoryCode, Integer> activityMapByCategoryCode = activityCache.getActivityMapByCategoryCode();
        Map<CategoryCode, Integer> result = new HashMap<>();
        result.put(CategoryCode.CA0001, activityMapByCategoryCode.getOrDefault(CategoryCode.CA0001, 0));
        result.put(CategoryCode.CA0002, activityMapByCategoryCode.getOrDefault(CategoryCode.CA0002, 0));
        result.put(CategoryCode.CA0003, activityMapByCategoryCode.getOrDefault(CategoryCode.CA0003, 0));
        result.put(CategoryCode.CA0004, activityMapByCategoryCode.getOrDefault(CategoryCode.CA0004, 0));
        result.put(CategoryCode.CA0005, activityMapByCategoryCode.getOrDefault(CategoryCode.CA0005, 0));
        result.put(CategoryCode.CA0006, activityMapByCategoryCode.getOrDefault(CategoryCode.CA0006, 0));
        result.put(CategoryCode.CA0007, activityMapByCategoryCode.getOrDefault(CategoryCode.CA0007, 0));
        result.put(CategoryCode.CA9999, activityMapByCategoryCode.getOrDefault(CategoryCode.CA9999, 0));

        return result;
    }

    public ActivityCache makeActivityCacheValue(Integer userId) {
        Map<CategoryCode, Integer> activityMapByCategoryCode = new HashMap<>();

        List<Classification> classificationList = classificationRepository.findAllByUser_UserId(userId);

        for (Classification classification : classificationList) {
            Set<String> keySet = classification.getCategory().keySet();
            for (String key : keySet) {
                CategoryCode categoryCode = CategoryCode.valueOf(key);
                if (activityMapByCategoryCode.containsKey(categoryCode)) {
                    activityMapByCategoryCode.put(categoryCode, activityMapByCategoryCode.get(categoryCode) + 1);
                } else {
                    activityMapByCategoryCode.put(categoryCode, 1);
                }
            }
        }
        return activityCacheService.updateCacheData(userId, activityMapByCategoryCode);
    }
}
