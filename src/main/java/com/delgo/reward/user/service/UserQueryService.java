package com.delgo.reward.user.service;


import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;

    public User getOneByEmail(String email) {
        return userRepository.findOneByEmail(email)
                .orElseThrow(() -> new NotFoundDataException("[User] email : " + email));
    }

    public User getOneByUserId(int userId) {
        return userRepository.findOneByUserId(userId)
                .orElseThrow(() -> new NotFoundDataException("[User] userId : " + userId));
    }

    public User getOneByPhoneNo(String phoneNo) {
        return userRepository.findOneByPhoneNo(phoneNo)
                .orElseThrow(() -> new NotFoundDataException("[User] phoneNo : " + phoneNo));
    }

    public User getOneByAppleUniqueNo(String appleUniqueNo) {
        return userRepository.findOneByAppleUniqueNo(appleUniqueNo)
                .orElseThrow(() -> new NotFoundDataException("[User] appleUniqueNo : " + appleUniqueNo));
    }

    public Page<User> getPagingListBySearchWord(String searchWord, Pageable pageable) {
        return userRepository.searchByName(searchWord, pageable);
    }

    public List<User> getListByPGeoCode(String pGeoCode) {
        return userRepository.findListBypGeoCode(pGeoCode);
    }

    public boolean isPhoneNoExisting(String phoneNo) {
        return userRepository.findOneByPhoneNo(phoneNo).isPresent();
    }

    public boolean isEmailExisting(String email) {
        return userRepository.findOneByEmail(email).isPresent();
    }

    public boolean isNameExisting(String name) {
        return userRepository.findOneByName(name).isPresent();
    }

    public boolean checkNotificationPermission(int userId) {
        User user = getOneByUserId(userId);
        boolean isNotify = user.getIsNotify();
        boolean hasFcmToken = StringUtils.isNotEmpty(user.getFcmToken());
        return !isNotify || !hasFcmToken;
    }

    public boolean isAppleUniqueNoExisting(String appleUniqueNo) {
        return userRepository.findOneByAppleUniqueNo(appleUniqueNo).isPresent();
    }
}
