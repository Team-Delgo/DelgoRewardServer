package com.delgo.reward.user.controller.response;

import com.delgo.reward.dto.comm.PageCustom;
import com.delgo.reward.user.domain.User;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageCustomSearchUserResponse extends PageCustom<User> {
    public PageCustomSearchUserResponse(List<User> data, int size, int number, boolean last) {
        super(data, size, number, last);
    }
}