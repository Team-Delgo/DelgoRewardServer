package com.delgo.reward.dto.cert;

import com.delgo.reward.comm.code.CategoryCode;
import lombok.Getter;


@Getter
public class CategoryCountDTO {
    private final CategoryCode categoryCode; // 멍플 고유 아이디
    private final int count; // 개수


    public CategoryCountDTO(CategoryCode categoryCode, Long count) {
        this.categoryCode = categoryCode;
        this.count = count.intValue(); // 형 변환
    }
}