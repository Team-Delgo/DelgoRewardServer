package com.delgo.reward.service.user;



import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.comm.PageSearchUserDTO;
import com.delgo.reward.dto.user.SearchUserResDTO;
import com.delgo.reward.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;


import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundDataException("[User] email : " + email));
    }

    public User getUserById(int userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundDataException("[User] userId : " + userId));
    }

    public User getUserByPhoneNo(String phoneNo) {
        return userRepository.findByPhoneNo(phoneNo)
                .orElseThrow(() -> new NotFoundDataException("[User] phoneNo : " + phoneNo));
    }

    public User getUserByAppleUniqueNo(String appleUniqueNo) {
        return userRepository.findByAppleUniqueNo(appleUniqueNo)
                .orElseThrow(() -> new NotFoundDataException("[User] appleUniqueNo : " + appleUniqueNo));
    }

    public boolean isPhoneNoExisting(String phoneNo) {
        return userRepository.findByPhoneNo(phoneNo).isPresent();
    }

    public boolean isEmailExisting(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isNameExisting(String name) {
        return userRepository.findByName(name).isPresent();
    }

    public boolean isAppleUniqueNoExisting(String appleUniqueNo) {
        Optional<User> findUser = userRepository.findByAppleUniqueNo(appleUniqueNo);
        return findUser.isPresent();
    }

    /**
     * 유저 검색 결과 반환
     */
    public PageSearchUserDTO getSearchUsers(String searchWord, Pageable pageable) {
        Slice<User> users = userRepository.searchByName(searchWord, pageable);
        List<SearchUserResDTO> resDTOs = users.getContent().stream().map(SearchUserResDTO::new).toList();

        return new PageSearchUserDTO(resDTOs, users.getSize(), users.getNumber(), users.isLast());
    }
}
