package com.delgo.reward.user.service.port;

import com.delgo.reward.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UserRepository {
    User save(User user);
    void delete(User user);
    User findByName(String name);
    User findByEmail(String email);
    User findByUserId(Integer userId);
    User findByPhoneNo(String phoneNo);
    User findByAppleUniqueNo(String appleUniqueNo);
    void increaseViewCount(int userId);


//    [정렬 조건] - 페이징으로 인해 Java가 아닌 Sql로 정렬한다.
//    User Name 검색어와 정확히 일치하면 -2로 정렬.
//    Pet Name 검색어와 정확히 일치하면 -1로 정렬.
//    if User Name contain searchWord, 두 길이 차이의 절대값을 구하고 1을 빼준다. ( 1빼주는 이유 Pet 보다 User를 우선하기 위해)
//    if Pet Name contain searchWord, 두 길이 차이의 절대값을 구한다.
//    Slice<User> searchByName(String keyword, Pageable pageable);
    Slice<User> searchByName(String keyword, Pageable pageable);
}