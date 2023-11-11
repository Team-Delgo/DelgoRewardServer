package com.delgo.reward.user.service;


import com.delgo.reward.cache.ActivityCache;
import com.delgo.reward.cacheService.ActivityCacheService;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.comm.oauth.KakaoService;
import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.user.controller.response.PageCustomSearchUserResponse;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoRepository.ClassificationRepository;
import com.delgo.reward.record.signup.OAuthSignUpRecord;
import com.delgo.reward.record.signup.SignUpRecord;
import com.delgo.reward.record.user.ModifyUserRecord;
import com.delgo.reward.repository.*;
import com.delgo.reward.certification.service.port.CertRepository;
import com.delgo.reward.service.ArchiveService;
import com.delgo.reward.service.CodeService;
import com.delgo.reward.service.PhotoService;
import com.delgo.reward.user.controller.response.UserResponse;
import com.delgo.reward.user.domain.CategoryCount;
import com.delgo.reward.user.domain.Pet;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.service.port.CategoryCountRepository;
import com.delgo.reward.user.service.port.PetRepository;
import com.delgo.reward.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@Builder
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    // Service
    private final PetService petService;
    private final CodeService codeService;
    private final TokenService tokenService;
    private final KakaoService kakaoService;
    private final PhotoService photoService;
    private final ArchiveService archiveService;
    private final ObjectStorageService objectStorageService;

    // Repository
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final CertRepository certRepository;
    private final LikeListRepository likeListRepository;
    private final CategoryCountRepository categoryCountRepository;
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
        String address = (signUpRecord.geoCode().equals("0"))  // 세종시는 구가 없음.
                ? codeService.getAddress(signUpRecord.pGeoCode(), true)
                : codeService.getAddress(signUpRecord.geoCode(), false);

        // USER & PET 객체 생성 + categoryCount 객체 생성
        User user = signUpRecord.makeUser(passwordEncoder.encode(signUpRecord.password()), address);
        Pet pet = signUpRecord.makePet(user);
        CategoryCount categoryCount = CategoryCount.create(user.getUserId());

        // 필요 값 적용
        user.setVersion(version);

        user.setProfile(profile.isEmpty()
                ? DEFAULT_PROFILE
                : photoService.uploadProfile(user.getUserId(), profile));
        user.setPet(pet);

        // USER & PET 엔티티 저장 + categoryCount 엔티티 저장
        userRepository.save(user);
        petRepository.save(pet);
        categoryCountRepository.save(CategoryCount.create(user.getUserId()));

        // WELCOME 업적 부여
        archiveService.registerWelcome(user.getUserId());
//        jdbcTemplatePointRepository.createUserPoint(user); // Point 생성
        //        rankingService.rankingByPoint(); // 랭킹 업데이트

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
        String address = (oAuthSignUpRecord.geoCode().equals("0"))  // 세종시는 구가 없음.
                ? codeService.getAddress(oAuthSignUpRecord.pGeoCode(), true)
                : codeService.getAddress(oAuthSignUpRecord.geoCode(), false);

        // USER & PET 객체 생성 + categoryCount 객체 생성
        User user = oAuthSignUpRecord.makeUser(oAuthSignUpRecord.userSocial(), address);
        Pet pet = oAuthSignUpRecord.makePet(user);
        CategoryCount categoryCount = CategoryCount.create(user.getUserId());
        // 필요 값 적용
        user.setVersion(version);
        user.setPet(pet);
        user.setProfile(profile.isEmpty()
                ? DEFAULT_PROFILE
                : photoService.uploadProfile(user.getUserId(), profile));
        // USER & PET 엔티티 저장 + categoryCount 엔티티 저장
        userRepository.save(user);
        petRepository.save(pet);
        categoryCountRepository.save(CategoryCount.create(user.getUserId()));

        // WELCOME 업적 부여
        archiveService.registerWelcome(user.getUserId());
//        jdbcTemplatePointRepository.createUserPoint(oAuthUser); // Point 생성

//        rankingService.rankingByPoint(); // 랭킹 업데이트
        return user;
    }

    /**
     * 회원탈퇴
     *
     * @param userId
     * @throws Exception
     */
    public void deleteUser(int userId) throws Exception {
        User user = getUserById(userId);
        if (user.getUserSocial().equals(UserSocial.K))
            kakaoService.logout(user.getKakaoId()); // kakao 로그아웃 , Naver는 로그아웃 지원 X

        certRepository.deleteByUserId(userId);
        likeListRepository.deleteByUserId(userId); // USER가 좋아요 누른 DATA 삭제
        categoryCountRepository.deleteByUserId(userId);

        jdbcTemplatePointRepository.deleteAllByUserId(userId);
        jdbcTemplateRankingRepository.deleteAllByUserId(userId);

        petRepository.delete(user.getPet());
        userRepository.delete(user);

        objectStorageService.deleteObject(BucketName.PROFILE, userId + "_profile.webp");
    }

    /**
     * 로그아웃
     *
     * @param userId
     * @throws Exception
     */
    public void logout(int userId) throws Exception {
        User user = getUserById(userId);
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
        User user = getUserByEmail(checkedEmail);
        user.setPassword(passwordEncoder.encode(newPassword));
    }


    /**
     * 전화번호 존재 여부 확인
     *
     * @param phoneNo
     * @return 존재 여부 반환
     */
    public boolean isPhoneNoExisting(String phoneNo) {
        return userRepository.findByPhoneNo(phoneNo) != null;
    }

    /**
     * 이메일 존재 여부 확인
     *
     * @param email
     * @return 존재 여부 반환
     */
    public boolean isEmailExisting(String email) {
        return userRepository.findByEmail(email) != null;
    }

    /**
     * 이름 존재 여부 확인
     *
     * @param name
     * @return 존재 여부 반환
     */
    public boolean isNameExisting(String name) {
        return userRepository.findByName(name) != null;
    }

    /**
     * 애플 유저인지 판단
     *
     * @param appleUniqueNo
     * @return 애플 유저 여부 반환
     */
    public boolean isAppleUniqueNoExisting(String appleUniqueNo) {
        return userRepository.findByAppleUniqueNo(appleUniqueNo) != null;
    }

    /**
     * 알림 동의 여부 수정
     *
     * @param userId
     * @return 수정된 알람 동의 여부 반환
     */
    public boolean changeNotify(int userId) {
        User user = getUserById(userId);
        user.setNotify();

        return userRepository.save(user).getIsNotify();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserById(int userId) {
        return userRepository.findByUserId(userId);
    }

    public User getUserByPhoneNo(String phoneNo) {
        return userRepository.findByPhoneNo(phoneNo);
    }

    public User getUserByAppleUniqueNo(String appleUniqueNo) {
        return userRepository.findByAppleUniqueNo(appleUniqueNo);
    }

    public User changePhoto(int userId, String ncpLink) {
        User user = getUserById(userId);
        user.setProfile(ncpLink);

        return userRepository.save(user);
    }

    /**
     * 유저 정보 수정
     *
     * @param modifyUserRecord
     * @return 수정된 유저 정보 반환
     */
    public User changeUserInfo(ModifyUserRecord modifyUserRecord, MultipartFile profile) {
        User user = getUserById(modifyUserRecord.userId());

        if (profile != null)
            user.setProfile(photoService.uploadProfile(user.getUserId(), profile));
        if (modifyUserRecord.geoCode() != null && modifyUserRecord.pGeoCode() != null) {
            user.setGeoCode(modifyUserRecord.geoCode());
            user.setPGeoCode(modifyUserRecord.pGeoCode());

            // 주소 설정
            String address = (modifyUserRecord.geoCode().equals("0"))  // 세종시는 구가 없음.
                    ? codeService.getAddress(modifyUserRecord.pGeoCode(), true)
                    : codeService.getAddress(modifyUserRecord.geoCode(), false);
            user.setAddress(address);

            jdbcTemplatePointRepository.changeGeoCode(user.getUserId(), modifyUserRecord.geoCode());
        }

        Optional.ofNullable(modifyUserRecord.name()).ifPresent(user::setName);
        return user;
    }

    /**
     * 유저별 카테고리 카운트 조회
     *
     * @param userId
     * @return 카테고리 카운트 반환
     */
    public CategoryCount getCategoryCountByUserId(int userId) {
        return categoryCountRepository.findByUserId(userId).orElseThrow(() -> new NullPointerException("NOT FOUND CategoryCount userId: " + userId));
    }

    /**
     * 카테고리 카운트 DB저장
     *
     * @param categoryCount
     * @return 저장된 카테고리 카운트
     */
    public CategoryCount categoryCountSave(CategoryCount categoryCount) {
        return categoryCountRepository.save(categoryCount);
    }


    /**
     * 유저 검색 결과 반환
     */
    public PageCustomSearchUserResponse getSearchUsers(String searchWord, Pageable pageable) {
        Slice<User> userSlice = userRepository.searchByName(searchWord, pageable);

        return new PageCustomSearchUserResponse(userSlice.getContent(), userSlice.getSize(), userSlice.getNumber(), userSlice.isLast());
    }

    public void increaseViewCount(int userId) {
        userRepository.increaseViewCount(userId);
    }

    public Map<CategoryCode, Integer> getActivityByUserId(Integer userId) {
        ActivityCache activityCache = activityCacheService.getCacheData(userId);

        if (!activityCacheService.isValidation(activityCache)) {
            activityCache = makeActivityCacheValue(userId);
        }

        return activityCache.getActivityMapByCategoryCode();
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
