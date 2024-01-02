package com.delgo.reward.service.user;


import com.delgo.reward.domain.user.User;
import com.delgo.reward.record.user.UserUpdate;
import com.delgo.reward.repository.UserRepository;
import com.delgo.reward.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {
    private final UserRepository userRepository;

    private final CodeService codeService;
    private final UserQueryService userQueryService;

    public User save(User user){
        return userRepository.save(user);
    }

    public User updateProfile(int userId, String url) {
        User user = userQueryService.getOneByUserId(userId);
        user.setProfile(url);

        return save(user);
    }

    public User updatePassword(String email, String encodedPassword) {
        User user = userQueryService.getOneByEmail(email);
        user.setPassword(encodedPassword);

        return save(user);
    }

    public User updateIsNotify(int userId) {
        User user = userQueryService.getOneByUserId(userId);
        user.setIsNotify(!user.getIsNotify());

        return save(user);
    }

    public User updateUserInfo(UserUpdate userUpdate) {
        User user = userQueryService.getOneByUserId(userUpdate.userId());

        if (userUpdate.geoCode() != null && userUpdate.pGeoCode() != null) {
            user.setGeoCode(userUpdate.geoCode());
            user.setPGeoCode(userUpdate.pGeoCode());

            // 주소 설정
            String address = codeService.getAddressByGeoCode(userUpdate.geoCode());
            user.setAddress(address);
        }

        Optional.ofNullable(userUpdate.name()).ifPresent(user::setName);
        return save(user);
    }

    public void deleteByUserId(int userId) {
        userRepository.deleteByUserId(userId);
    }

    public void increaseViewCount(int userId) {
        userRepository.increaseViewCount(userId);
    }
}
