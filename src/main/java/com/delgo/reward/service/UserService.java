package com.delgo.reward.service;


import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.comm.oauth.KakaoService;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import com.delgo.reward.dto.user.ModifyUserDTO;
import com.delgo.reward.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // JDBC Templates
    private final JDBCTemplatePointRepository jdbcTemplatePointRepository;
    private final JDBCTemplateRankingRepository jdbcTemplateRankingRepository;

    // DB 저장
    public User save(User user) {
       return userRepository.save(user);
    }

    // 회원가입
    public User signup(User user, MultipartFile profile) {
        User registeredUser = save(user);
        jdbcTemplatePointRepository.createUserPoint(registeredUser); // Point 생성

        return registeredUser.setProfile(photoService.uploadProfile(user.getUserId(), profile)); // User Profile 등록
    }

    // 회원탈퇴
    public void deleteUser(int userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
        Pet pet = petRepository.findByUserId(userId).orElseThrow(() -> new NullPointerException("NOT FOUND PET"));

        if (user.getUserSocial().equals(UserSocial.K))
            kakaoService.logout(user.getKakaoId()); // kakao 로그아웃

        commentRepository.deleteAllByUserId(userId);
        certRepository.deleteAllByUserId(userId);
        likeListRepository.deleteByUserId(userId);; // USER가 좋아요 누른 DATA 삭제

        jdbcTemplateRankingRepository.deleteAllByUserId(userId);
        jdbcTemplatePointRepository.deleteAllByUserId(userId);

        petRepository.delete(pet);
        userRepository.delete(user);

        objectStorageService.deleteObject(BucketName.PROFILE, userId + "_profile.webp");
    }

    // 비밀번호 변경
    public void changePassword(String checkedEmail, String newPassword) {
        User user = userRepository.findByEmail(checkedEmail).orElseThrow(() -> new IllegalArgumentException("The email does not exist"));
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }


    // 전화번호 존재 유무 확인
    public boolean isPhoneNoExisting(String phoneNo) {
        Optional<User> findUser = userRepository.findByPhoneNo(phoneNo);
        return findUser.isPresent();
    }

    // 이메일 존재 유무 확인
    public boolean isEmailExisting(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        return findUser.isPresent();
    }

    // 이름 존재 유무 확인
    public boolean isNameExisting(String name) {
        Optional<User> findUser = userRepository.findByName(name);
        return findUser.isPresent();
    }

    // 애플 연동 유무 확인
    public boolean isAppleUniqueNoExisting(String appleUniqueNo) {
        Optional<User> findUser = userRepository.findByAppleUniqueNo(appleUniqueNo);
        return findUser.isPresent();
    }

    // 알림 정보 수정
    public boolean changeNotify(int userId){
        userRepository.updateNotify(userId, !getUserById(userId).isNotify());
        return getUserById(userId).setNotify();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
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

    public User changeUserInfo(ModifyUserDTO modifyUserDTO) {
        User user = getUserById(modifyUserDTO.getUserId());

        if (modifyUserDTO.getName() != null)
            user.setName(modifyUserDTO.getName());

        if (modifyUserDTO.getProfileUrl() != null)
            user.setProfile(modifyUserDTO.getProfileUrl());

        if (modifyUserDTO.getGeoCode() != null && modifyUserDTO.getPGeoCode() != null) {
            user.setGeoCode(modifyUserDTO.getGeoCode());
            user.setPGeoCode(modifyUserDTO.getPGeoCode());

            // 주소 설정
            String address = (modifyUserDTO.getGeoCode().equals("0"))  // 세종시는 구가 없음.
                    ? codeService.getAddress(modifyUserDTO.getPGeoCode(), true)
                    : codeService.getAddress(modifyUserDTO.getGeoCode(), false);
            user.setAddress(address);

            jdbcTemplatePointRepository.changeGeoCode(user.getUserId(), modifyUserDTO.getGeoCode());
        }

        return user;
    }
}
