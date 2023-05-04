package com.delgo.reward.service;


import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.comm.oauth.KakaoService;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.CategoryCount;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import com.delgo.reward.record.signup.OAuthSignUpRecord;
import com.delgo.reward.record.signup.SignUpRecord;
import com.delgo.reward.record.user.ModifyUserRecord;
import com.delgo.reward.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    // Service
    private final PetService petService;
    private final CodeService codeService;
    private final KakaoService kakaoService;
    private final PhotoService photoService;
    private final ArchiveService archiveService;
    private final ObjectStorageService objectStorageService;

    // Repository
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final CertRepository certRepository;
    private final CommentRepository commentRepository;
    private final LikeListRepository likeListRepository;
    private final CategoryCountRepository categoryCountRepository;

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
     * @param signUpRecord
     * @param profile
     * @return 가입한 회원 정보 반환
     */
    @Transactional
    public User signup(SignUpRecord signUpRecord, MultipartFile profile) {
        // 주소 설정
        String address = (signUpRecord.geoCode().equals("0"))  // 세종시는 구가 없음.
                ? codeService.getAddress(signUpRecord.pGeoCode(), true)
                : codeService.getAddress(signUpRecord.geoCode(), false);

        // USER & PET 저장
        User user = save(signUpRecord.makeUser(passwordEncoder.encode(signUpRecord.password()), address));
        Pet pet = petService.register(signUpRecord.makePet(user));

        archiveService.registerWelcome(user.getUserId()); // WELCOME 업적 부여
        jdbcTemplatePointRepository.createUserPoint(user); // Point 생성
        categoryCountRepository.save(new CategoryCount().create(user.getUserId()));

//        rankingService.rankingByPoint(); // 랭킹 업데이트
        return user.setPet(pet).setProfile( // User Profile 등록
                profile.isEmpty()
                        ? DEFAULT_PROFILE
                        : photoService.uploadProfile(user.getUserId(), profile));
    }

    /**
     * OAuth 회원가입
     * @param oAuthSignUpRecord
     * @param profile
     * @return 가입한 회원 정보 반환
     */
    public User oAuthSignup(OAuthSignUpRecord oAuthSignUpRecord, MultipartFile profile) {
        // 주소 설정
        String address = (oAuthSignUpRecord.geoCode().equals("0"))  // 세종시는 구가 없음.
                ? codeService.getAddress(oAuthSignUpRecord.pGeoCode(), true)
                : codeService.getAddress(oAuthSignUpRecord.geoCode(), false);

        // USER & PET 저장
        User oAuthUser = save(oAuthSignUpRecord.makeUser(oAuthSignUpRecord.userSocial(), address));
        Pet pet = petService.register(oAuthSignUpRecord.makePet(oAuthUser));

        archiveService.registerWelcome(oAuthUser.getUserId()); // WELCOME 업적 부여
        jdbcTemplatePointRepository.createUserPoint(oAuthUser); // Point 생성
        categoryCountRepository.save(new CategoryCount().create(oAuthUser.getUserId()));

//        rankingService.rankingByPoint(); // 랭킹 업데이트
        return oAuthUser.setPet(pet).setProfile( // User Profile 등록
                profile.isEmpty()
                        ? DEFAULT_PROFILE
                        : photoService.uploadProfile(oAuthUser.getUserId(), profile));
    }

    /**
     * 회원탈퇴
     * @param userId
     * @throws Exception
     */
    public void deleteUser(int userId) throws Exception {
        User user = getUserById(userId);
        if (user.getUserSocial().equals(UserSocial.K))
            kakaoService.logout(user.getKakaoId()); // kakao 로그아웃

        certRepository.deleteAllByUserUserId(userId);
        commentRepository.deleteAllByUserId(userId);
        likeListRepository.deleteByUserId(userId); // USER가 좋아요 누른 DATA 삭제
        categoryCountRepository.deleteByUserId(userId);

        jdbcTemplatePointRepository.deleteAllByUserId(userId);
        jdbcTemplateRankingRepository.deleteAllByUserId(userId);

        petRepository.delete(user.getPet());
        userRepository.delete(user);

        objectStorageService.deleteObject(BucketName.PROFILE, userId + "_profile.webp");
    }

    /**
     * 비밀번호 변경
     * @param checkedEmail
     * @param newPassword
     */
    public void changePassword(String checkedEmail, String newPassword) {
        User user = getUserByEmail(checkedEmail);
        user.setPassword(passwordEncoder.encode(newPassword));
    }


    /**
     * 전화번호 존재 여부 확인
     * @param phoneNo
     * @return 존재 여부 반환
     */
    public boolean isPhoneNoExisting(String phoneNo) {
        return userRepository.findByPhoneNo(phoneNo).isPresent();
    }

    /**
     * 이메일 존재 여부 확인
     * @param email
     * @return 존재 여부 반환
     */
    public boolean isEmailExisting(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * 이름 존재 여부 확인
     * @param name
     * @return 존재 여부 반환
     */
    public boolean isNameExisting(String name) {
        return userRepository.findByName(name).isPresent();
    }

    /**
     * 애플 유저인지 판단
     * @param appleUniqueNo
     * @return 애플 유저 여부 반환
     */
    public boolean isAppleUniqueNoExisting(String appleUniqueNo) {
        Optional<User> findUser = userRepository.findByAppleUniqueNo(appleUniqueNo);
        return findUser.isPresent();
    }

    /**
     * 알림 동의 여부 수정
     * @param userId
     * @return 수정된 알람 동의 여부 반환
     */
    public boolean changeNotify(int userId){
        return getUserById(userId).setNotify();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
    }

    public Page<User> getUserAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserById(int userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
    }

    public User getUserByPhoneNo(String phoneNo) {
        return userRepository.findByPhoneNo(phoneNo)
                .orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
    }

    public User getUserByAppleUniqueNo(String appleUniqueNo) {
        return userRepository.findByAppleUniqueNo(appleUniqueNo)
                .orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
    }

    public User changePhoto(int userId, String ncpLink){
        return getUserById(userId).setProfile(ncpLink);
    }

    /**
     * 유저 정보 수정
     * @param modifyUserRecord
     * @return 수정된 유저 정보 반환
     */
    public User changeUserInfo(ModifyUserRecord modifyUserRecord) {
        User user = getUserById(modifyUserRecord.userId());

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
     * @param userId
     * @return 카테고리 카운트 반환
     */
    public CategoryCount getCategoryCountByUserId(int userId){
        return categoryCountRepository.findByUserId(userId).orElseThrow(() -> new NullPointerException("NOT FOUND CategoryCount userId: " + userId));
    }

    /**
     * 카테고리 카운트 DB저장
     * @param categoryCount
     * @return 저장된 카테고리 카운트
     */
    public CategoryCount categoryCountSave(CategoryCount categoryCount){
        return categoryCountRepository.save(categoryCount);
    }
}
