package com.delgo.reward.repository;


import com.delgo.reward.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNo(String phoneNo);
    Optional<User> findByName(String name);
    Optional<User> findByAppleUniqueNo(String appleUniqueNo);

    @Query(value = "update user set is_notify = :isNotify where user_id = :userId", nativeQuery = true)
    void updateNotify(int userId, boolean isNotify);

    Page<User> findAll(Pageable pageable);
}
