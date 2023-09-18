package com.delgo.reward.dto.mungple.detail;

import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import lombok.Getter;
import lombok.ToString;

import java.util.List;


@Getter
@ToString
public class MungpleDetailByPriceTagResDTO extends MungpleDetailResDTO {
    private final Boolean isPriceTag; // 가격표 존재 여부
    private final List<String> priceTagPhotoUrls; // 가격표 사진

    public MungpleDetailByPriceTagResDTO(MongoMungple mongoMungple, int certCount, boolean isBookmarked){
        super(mongoMungple,certCount, isBookmarked);

        isPriceTag = mongoMungple.getIsPriceTag();
        priceTagPhotoUrls = mongoMungple.getPriceTagPhotoUrls();
    }
}
