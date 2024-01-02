package com.delgo.reward.service.user;


import com.delgo.reward.cache.ActivityCache;
import com.delgo.reward.cacheService.ActivityCacheService;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.encoder.CustomPasswordEncoder;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoRepository.ClassificationRepository;
import com.delgo.reward.record.user.UserUpdate;
import com.delgo.reward.repository.UserRepository;
import com.delgo.reward.service.CodeService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Builder
@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {
    // Repository
    private final UserRepository userRepository;
    private final ClassificationRepository classificationRepository;

    // Service
    private final CodeService codeService;
    private final UserQueryService userQueryService;

    // Cache
    private final ActivityCacheService activityCacheService;


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
