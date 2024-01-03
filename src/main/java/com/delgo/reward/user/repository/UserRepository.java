package com.delgo.reward.user.repository;


import com.delgo.reward.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    void deleteByUserId(int userId);

    Optional<User> findOneByName(String name);
    Optional<User> findOneByEmail(String email);
    Optional<User> findOneByUserId(Integer userId);
    Optional<User> findOneByPhoneNo(String phoneNo);
    Optional<User> findOneByAppleUniqueNo(String appleUniqueNo);

    @Modifying
    @Query("UPDATE User u SET u.viewCount = u.viewCount + 1 WHERE u.userId = :userId")
    void increaseViewCount(@Param("userId") int userId);


//    [정렬 조건] - 페이징으로 인해 Java가 아닌 Sql로 정렬한다.
//    User Name 검색어와 정확히 일치하면 -2로 정렬.
//    Pet Name 검색어와 정확히 일치하면 -1로 정렬.
//    if User Name contain searchWord, 두 길이 차이의 절대값을 구하고 1을 빼준다. ( 1빼주는 이유 Pet 보다 User를 우선하기 위해)
//    if Pet Name contain searchWord, 두 길이 차이의 절대값을 구한다.
    @EntityGraph(attributePaths = {"pet"})
    @Query("SELECT u FROM User u WHERE u.name LIKE CONCAT('%', :keyword, '%') or u.pet.name LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY CASE " +
            "WHEN u.name = :keyword THEN -4 " +
            "WHEN u.pet.name = :keyword THEN -3 " +
            "WHEN u.name LIKE CONCAT(:keyword, '%') THEN -2 " +
            "WHEN u.pet.name LIKE CONCAT(:keyword, '%') THEN -1 " +
            "WHEN u.name LIKE CONCAT('%', :keyword, '%') THEN ABS(LENGTH(u.name) - LENGTH(:keyword) - 1) " +
            "WHEN u.pet.name LIKE CONCAT('%', :keyword, '%') THEN ABS(LENGTH(u.pet.name) - LENGTH(:keyword)) " +
            "ELSE 100 END")
    Page<User> searchByName(@Param("keyword") String keyword, Pageable pageable);
}