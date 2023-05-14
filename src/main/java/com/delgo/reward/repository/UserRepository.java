package com.delgo.reward.repository;


import com.delgo.reward.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(Integer userId);
    Optional<User> findByPhoneNo(String phoneNo);
    Optional<User> findByAppleUniqueNo(String appleUniqueNo);

    @EntityGraph(attributePaths = "pet")
    Page<User> findAll(Pageable pageable);
}
