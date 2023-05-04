package com.delgo.reward;


import com.delgo.reward.domain.user.User;
import com.delgo.reward.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    public void getUserTest() {
        //given

        //when
//        Page<User> users = userService.getUserAll(0,3);
        User user = userService.getUserById(1);

        //then
        System.out.println("user : " + user.getUserId());
        System.out.println("pet : " + user.getPet());
    }

}