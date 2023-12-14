package com.delgo.reward.dto.mungple.detail;

import com.delgo.reward.mongoDomain.mungple.Mungple;
import lombok.Getter;
import lombok.ToString;

import java.util.List;


@Getter
@ToString
public class MungpleDetailByPriceTagResDTO extends MungpleDetailResDTO {
    private final Boolean isPriceTag; // 가격표 존재 여부
    private final List<String> priceTagPhotoUrls; // 가격표 사진

    public MungpleDetailByPriceTagResDTO(Mungple mungple, int certCount, int bookmarkCount, boolean isBookmarked) {
        super(mungple, certCount, bookmarkCount, isBookmarked);

        isPriceTag = mungple.getIsPriceTag();
        priceTagPhotoUrls = mungple.getPriceTagPhotoUrls();
    }
}
