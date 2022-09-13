package com.delgo.reward.repository;


import com.delgo.reward.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNo(String phoneNo);
    Optional<User> findByUserId(int userId);
    Optional<User> findByName(String name);
}
