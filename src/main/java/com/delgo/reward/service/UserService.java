package com.delgo.reward.service;


import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.comm.oauth.KakaoService;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.CategoryCount;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import com.delgo.reward.record.user.ModifyUserRecord;
import com.delgo.reward.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final CodeService codeService;
    private final KakaoService kakaoService;
    private final PhotoService photoService;
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
     * @param user
     * @param profile
     * @return 가입한 회원 정보 반환
     */
    public User signup(User user, MultipartFile profile) {
        User registeredUser = save(user);
        jdbcTemplatePointRepository.createUserPoint(registeredUser); // Point 생성
        categoryCountRepository.save(new CategoryCount().create(registeredUser.getUserId()));

        return registeredUser.setProfile( // User Profile 등록
                profile.isEmpty()
                        ? DEFAULT_PROFILE
                        : photoService.uploadProfile(user.getUserId(), profile));
    }

    /**
     * 회원탈퇴
     * @param userId
     * @throws Exception
     */
    public void deleteUser(int userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
        Pet pet = petRepository.findByUserId(userId).orElseThrow(() -> new NullPointerException("NOT FOUND PET"));

        if (user.getUserSocial().equals(UserSocial.K))
            kakaoService.logout(user.getKakaoId()); // kakao 로그아웃

        commentRepository.deleteAllByUserId(userId);
        certRepository.deleteAllByUserUserId(userId);
        likeListRepository.deleteByUserId(userId);; // USER가 좋아요 누른 DATA 삭제

        jdbcTemplateRankingRepository.deleteAllByUserId(userId);
        jdbcTemplatePointRepository.deleteAllByUserId(userId);

        categoryCountRepository.deleteByUserId(userId);

        petRepository.delete(pet);
        userRepository.delete(user);

        objectStorageService.deleteObject(BucketName.PROFILE, userId + "_profile.webp");
    }

    /**
     * 비밀번호 변경
     * @param checkedEmail
     * @param newPassword
     */
    public void changePassword(String checkedEmail, String newPassword) {
        User user = userRepository.findByEmail(checkedEmail).orElseThrow(() -> new IllegalArgumentException("The email does not exist"));
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }


    /**
     * 전화번호 존재 여부 확인
     * @param phoneNo
     * @return 존재 여부 반환
     */
    public boolean isPhoneNoExisting(String phoneNo) {
        Optional<User> findUser = userRepository.findByPhoneNo(phoneNo);
        return findUser.isPresent();
    }

    /**
     * 이메일 존재 여부 확인
     * @param email
     * @return 존재 여부 반환
     */
    public boolean isEmailExisting(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        return findUser.isPresent();
    }

    /**
     * 이름 존재 여부 확인
     * @param name
     * @return 존재 여부 반환
     */
    public boolean isNameExisting(String name) {
        Optional<User> findUser = userRepository.findByName(name);
        return findUser.isPresent();
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
        userRepository.updateNotify(userId, !getUserById(userId).isNotify());
        return getUserById(userId).setNotify();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
    }

    public Page<User> getUserAll(int currentPage, int pageSize) {
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by("registDt").descending()); // 내림차순 정렬

        return userRepository.findAll(pageRequest);
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId)
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

        if (modifyUserRecord.name() != null)
            user.setName(modifyUserRecord.name());

        if (modifyUserRecord.profileUrl() != null)
            user.setProfile(modifyUserRecord.profileUrl());

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
