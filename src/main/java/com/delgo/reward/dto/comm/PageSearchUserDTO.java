package com.delgo.reward.dto.comm;

import com.delgo.reward.dto.user.SearchUserResDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class PageSearchUserDTO extends PageResponse<SearchUserResDTO> {
    public PageSearchUserDTO(List<SearchUserResDTO> data, int size, int number, boolean last) {
        super(data, size, number, last);
    }
}