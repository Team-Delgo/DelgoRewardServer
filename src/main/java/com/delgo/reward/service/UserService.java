package com.delgo.reward.service;


import com.delgo.reward.cache.ActivityCache;
import com.delgo.reward.cacheService.ActivityCacheService;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.comm.oauth.KakaoService;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.CategoryCount;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.dto.comm.PageSearchUserDTO;
import com.delgo.reward.dto.user.SearchUserResDTO;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoRepository.ClassificationRepository;
import com.delgo.reward.record.signup.OAuthSignUpRecord;
import com.delgo.reward.record.signup.SignUpRecord;
import com.delgo.reward.record.user.ModifyUserRecord;
import com.delgo.reward.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    // Service
    private final PetService petService;
    private final CodeService codeService;
    private final TokenService tokenService;
    private final KakaoService kakaoService;
    private final PhotoService photoService;
    private final ObjectStorageService objectStorageService;

    // Repository
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final CertRepository certRepository;
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
        String address = codeService.getAddressByGeoCode(signUpRecord.geoCode());

        // USER & PET 저장
        User user = save(signUpRecord.makeUser(passwordEncoder.encode(signUpRecord.password()), address));
        Pet pet = petService.register(signUpRecord.makePet(user));

        jdbcTemplatePointRepository.createUserPoint(user); // Point 생성
        categoryCountRepository.save(new CategoryCount().create(user.getUserId()));

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
        categoryCountRepository.save(new CategoryCount().create(oAuthUser.getUserId()));

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
        return userRepository.findByPhoneNo(phoneNo).isPresent();
    }

    /**
     * 이메일 존재 여부 확인
     *
     * @param email
     * @return 존재 여부 반환
     */
    public boolean isEmailExisting(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * 이름 존재 여부 확인
     *
     * @param name
     * @return 존재 여부 반환
     */
    public boolean isNameExisting(String name) {
        return userRepository.findByName(name).isPresent();
    }

    /**
     * 애플 유저인지 판단
     *
     * @param appleUniqueNo
     * @return 애플 유저 여부 반환
     */
    public boolean isAppleUniqueNoExisting(String appleUniqueNo) {
        Optional<User> findUser = userRepository.findByAppleUniqueNo(appleUniqueNo);
        return findUser.isPresent();
    }

    /**
     * 알림 동의 여부 수정
     *
     * @param userId
     * @return 수정된 알람 동의 여부 반환
     */
    public boolean changeNotify(int userId) {
        return getUserById(userId).setNotify();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundDataException("[User] email : " + email));
    }

    public User getUserById(int userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundDataException("[User] userId : " + userId));
    }

    public User getUserByPhoneNo(String phoneNo) {
        return userRepository.findByPhoneNo(phoneNo)
                .orElseThrow(() -> new NotFoundDataException("[User] phoneNo : " + phoneNo));
    }

    public User getUserByAppleUniqueNo(String appleUniqueNo) {
        return userRepository.findByAppleUniqueNo(appleUniqueNo)
                .orElseThrow(() -> new NotFoundDataException("[User] appleUniqueNo : " + appleUniqueNo));
    }

    public User changePhoto(int userId, String ncpLink) {
        return getUserById(userId).setProfile(ncpLink);
    }

    /**
     * 유저 정보 수정
     *
     * @param modifyUserRecord
     * @return 수정된 유저 정보 반환
     */
    public User changeUserInfo(ModifyUserRecord modifyUserRecord) {
        User user = getUserById(modifyUserRecord.userId());

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

    /**
     * 유저별 카테고리 카운트 조회
     *
     * @param userId
     * @return 카테고리 카운트 반환
     */
    public CategoryCount getCategoryCountByUserId(int userId) {
        return categoryCountRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundDataException("[CategoryCount] userId : " + userId));
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
    public PageSearchUserDTO getSearchUsers(String searchWord, Pageable pageable) {
        Slice<User> users = userRepository.searchByName(searchWord, pageable);
        List<SearchUserResDTO> resDTOs = users.getContent().stream().map(SearchUserResDTO::new).toList();

        return new PageSearchUserDTO(resDTOs, users.getSize(), users.getNumber(), users.isLast());
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
