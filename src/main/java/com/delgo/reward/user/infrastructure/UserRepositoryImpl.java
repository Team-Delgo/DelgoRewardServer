package com.delgo.reward.user.infrastructure;

import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.infrastructure.entity.UserEntity;
import com.delgo.reward.user.infrastructure.jpa.UserJpaRepository;
import com.delgo.reward.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    private User getOrThrow(Optional<UserEntity> userEntityOptional){
        return userEntityOptional.orElseThrow(() -> new NullPointerException("NOT FOUND USER")).toModel();
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.from(user)).toModel();
    }

    @Override
    public void delete(User user) {
        userJpaRepository.delete(UserEntity.from(user));
    }

    @Override
    public User findByName(String name) {
        return getOrThrow(userJpaRepository.findByName(name));
    }

    @Override
    public User findByEmail(String email) {
        return getOrThrow(userJpaRepository.findByEmail(email));
    }

    @Override
    public User findByUserId(Integer userId) {
        return getOrThrow(userJpaRepository.findByUserId(userId));
    }

    @Override
    public User findByPhoneNo(String phoneNo) {
        return getOrThrow(userJpaRepository.findByPhoneNo(phoneNo));
    }

    @Override
    public User findByAppleUniqueNo(String appleUniqueNo) {
        return getOrThrow(userJpaRepository.findByAppleUniqueNo(appleUniqueNo));
    }

    @Override
    public void increaseViewCount(int userId) {
        userJpaRepository.increaseViewCount(userId);
    }

    @Override
    public Slice<User> searchByName(String keyword, Pageable pageable) {
        Slice<UserEntity> userEntitySlice = userJpaRepository.searchByName(keyword, pageable);
        Pageable userEntitySlicePageable = userEntitySlice.getPageable();
        boolean userEntitySliceHasNext = userEntitySlice.hasNext();
        List<UserEntity> userEntityList = userEntitySlice.getContent();

        List<User> userList = userJpaRepository.searchByName(keyword, pageable).getContent()
                .stream()
                .map((UserEntity::toModel))
                .toList();

        return new SliceImpl<>(userEntityList.stream().map((UserEntity::toModel)).toList(), userEntitySlicePageable, userEntitySliceHasNext);
    }
}
