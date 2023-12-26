package com.delgo.reward.service.user;


import com.delgo.reward.cache.ActivityCache;
import com.delgo.reward.cacheService.ActivityCacheService;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoRepository.ClassificationRepository;
import com.delgo.reward.record.signup.OAuthSignUpRecord;
import com.delgo.reward.record.signup.SignUpRecord;
import com.delgo.reward.record.user.ModifyUserRecord;
import com.delgo.reward.repository.UserRepository;
import com.delgo.reward.service.CodeService;
import com.delgo.reward.service.PetService;
import com.delgo.reward.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {
    private final PasswordEncoder passwordEncoder;

    // Service
    private final PetService petService;
    private final CodeService codeService;
    private final PhotoService photoService;
    private final UserQueryService userQueryService;

    // Repository
    private final UserRepository userRepository;
    private final ClassificationRepository classificationRepository;

    // Cache
    private final ActivityCacheService activityCacheService;

    // 기본 프로필
    private final String DEFAULT_PROFILE = "https://kr.object.ncloudstorage.com/reward-profile/%EA%B8%B0%EB%B3%B8%ED%94%84%EB%A1%9C%ED%95%84.webp";

    public User signup(SignUpRecord signUpRecord, MultipartFile profile, String version) {
        String address = codeService.getAddressByGeoCode(signUpRecord.geoCode());
        String password = passwordEncoder.encode(signUpRecord.password());

        User user = userRepository.save(User.from(signUpRecord, password, address, version));
        user.setPet(petService.register(Pet.from(signUpRecord, user)));
        user.setProfile(createProfile(user.getUserId(), profile));
        return user;
    }

    public User OAuthSignup(OAuthSignUpRecord oAuthSignUpRecord, MultipartFile profile, String version) {
        String address = codeService.getAddressByGeoCode(oAuthSignUpRecord.geoCode());
        User user = userRepository.save(User.from(oAuthSignUpRecord, address, version));
        user.setPet(petService.register(Pet.from(oAuthSignUpRecord, user)));
        user.setProfile(createProfile(user.getUserId(), profile));
        return user;
    }

    public String createProfile(int userId, MultipartFile profile) {
        if (profile.isEmpty()) return DEFAULT_PROFILE;

        String fileName = photoService.makeProfileFileName(userId, profile);
        return photoService.saveAndUpload(fileName, profile, BucketName.PROFILE);
    }

    public void delete(int userId) {
        userRepository.deleteByUserId(userId);
    }


    public void changePassword(String checkedEmail, String newPassword) {
        User user = userQueryService.getOneByEmail(checkedEmail);
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    public boolean changeNotify(int userId) {
        return userQueryService.getOneByUserId(userId).setNotify();
    }

    public User changePhoto(int userId, String ncpLink) {
        return userQueryService.getOneByUserId(userId).setProfile(ncpLink);
    }

    public User changeUserInfo(ModifyUserRecord modifyUserRecord) {
        User user = userQueryService.getOneByUserId(modifyUserRecord.userId());

        if (modifyUserRecord.geoCode() != null && modifyUserRecord.pGeoCode() != null) {
            user.setGeoCode(modifyUserRecord.geoCode());
            user.setPGeoCode(modifyUserRecord.pGeoCode());

            // 주소 설정
            String address = codeService.getAddressByGeoCode(modifyUserRecord.geoCode());
            user.setAddress(address);
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
