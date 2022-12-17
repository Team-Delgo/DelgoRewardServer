package com.delgo.reward.service;


import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.user.ModifyUserDTO;
import com.delgo.reward.dto.user.UserInfoDTO;
import com.delgo.reward.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final CodeService codeService;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final CertRepository certRepository;
    private final JDBCTemplatePointRepository jdbcTemplatePointRepository;
    private final JDBCTemplateRankingRepository jdbcTemplateRankingRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public User signup(User user, Pet pet) {
        // 패스워드 암호화 및 적용
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);
        // User Data save
        User owner = userRepository.save(user);
        jdbcTemplatePointRepository.createUserPoint(user);
        // Pet Data save
        pet.setUserId(owner.getUserId());
        petRepository.save(pet);

        return owner;
    }

    // 회원탈퇴
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
        Pet pet = petRepository.findByUserId(userId).orElseThrow(() -> new NullPointerException("NOT FOUND PET"));

        commentRepository.deleteAllByUserId(userId);
        certRepository.deleteAllByUserId(userId);

        jdbcTemplateRankingRepository.deleteAllByUserId(userId);
        jdbcTemplatePointRepository.deleteAllByUserId(userId);

        petRepository.delete(pet);
        userRepository.delete(user);
    }

    // 비밀번호 변경
    public void changePassword(String checkedEmail, String newPassword) {
        User user = userRepository.findByEmail(checkedEmail).orElseThrow(() -> new IllegalArgumentException("The " +
                "email does not exist"));
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
        User user = getUserById(userId);
        user.setProfile(ncpLink);
        return userRepository.save(user);
    }

    public User changeUserInfo(ModifyUserDTO modifyUserDTO) {
        User originUser = getUserByEmail(modifyUserDTO.getEmail());

        if (modifyUserDTO.getName() != null)
            originUser.setName(modifyUserDTO.getName());

        if (modifyUserDTO.getProfileUrl() != null)
            originUser.setProfile(modifyUserDTO.getProfileUrl());

        if (modifyUserDTO.getGeoCode() != null && modifyUserDTO.getPGeoCode() != null) {
            originUser.setGeoCode(modifyUserDTO.getGeoCode());
            originUser.setPGeoCode(modifyUserDTO.getPGeoCode());

            // 주소 설정
            String address = (modifyUserDTO.getGeoCode().equals("0"))  // 세종시는 구가 없음.
                    ? codeService.getAddress(modifyUserDTO.getPGeoCode(), true)
                    : codeService.getAddress(modifyUserDTO.getGeoCode(), false);
            originUser.setAddress(address);

        }

        jdbcTemplatePointRepository.changeGeoCode(originUser.getUserId(), modifyUserDTO.getGeoCode());
        return userRepository.save(originUser);
    }

    public UserInfoDTO getUserInfo(int userId){
        User user = getUserById(userId);
        UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                .userName(user.getName())
                .profile(user.getProfile())
                .build();
        return userInfoDTO;
    }
}
