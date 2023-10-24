package com.delgo.reward.dto.comm;

import com.delgo.reward.dto.user.SearchUserResDTO;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageCustomSearchUserDTO extends PageCustom<SearchUserResDTO> {
    public PageCustomSearchUserDTO(List<SearchUserResDTO> data, int size, int number, boolean last) {
        super(data, size, number, last);
    }
}