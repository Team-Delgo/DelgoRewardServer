package com.delgo.reward.service.user;


import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;

    public User getOneByEmail(String email) {
        return userRepository.findOneByEmail(email)
                .orElseThrow(() -> new NotFoundDataException("[User] email : " + email));
    }

    public User getOneByUserId(int userId) {
        return userRepository.findOneByUserId(userId)
                .orElseThrow(() -> new NotFoundDataException("[User] userId : " + userId));
    }

    public User getOneByPhoneNo(String phoneNo) {
        return userRepository.findOneByPhoneNo(phoneNo)
                .orElseThrow(() -> new NotFoundDataException("[User] phoneNo : " + phoneNo));
    }

    public User getOneByAppleUniqueNo(String appleUniqueNo) {
        return userRepository.findOneByAppleUniqueNo(appleUniqueNo)
                .orElseThrow(() -> new NotFoundDataException("[User] appleUniqueNo : " + appleUniqueNo));
    }

    public Page<User> getPagingListBySearchWord(String searchWord, Pageable pageable) {
        return userRepository.searchByName(searchWord, pageable);
    }

    public boolean isPhoneNoExisting(String phoneNo) {
        return userRepository.findOneByPhoneNo(phoneNo).isPresent();
    }

    public boolean isEmailExisting(String email) {
        return userRepository.findOneByEmail(email).isPresent();
    }

    public boolean isNameExisting(String name) {
        return userRepository.findOneByName(name).isPresent();
    }

    public boolean isAppleUniqueNoExisting(String appleUniqueNo) {
        return userRepository.findOneByAppleUniqueNo(appleUniqueNo).isPresent();
    }
}
