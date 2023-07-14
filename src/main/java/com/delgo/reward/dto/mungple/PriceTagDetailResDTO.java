package com.delgo.reward.dto.mungple;

import com.delgo.reward.domain.mungple.Mungple;
import com.delgo.reward.mongoDomain.MungpleDetail;
import lombok.Getter;
import lombok.ToString;

import java.util.List;


@Getter
@ToString
public class PriceTagDetailResDTO extends MungpleDetailResDTO {
    private final Boolean isPriceTag; // 가격표 존재 여부
    private final List<String> priceTagPhotoUrls; // 가격표 사진


    public PriceTagDetailResDTO(Mungple mungple, MungpleDetail mungpleDetail, int certCount){
        super(mungple,mungpleDetail,certCount);

        isPriceTag = mungpleDetail.getIsPriceTag();
        priceTagPhotoUrls = mungpleDetail.getPriceTagPhotoUrls();
    }
}
