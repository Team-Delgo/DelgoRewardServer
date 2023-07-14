package com.delgo.reward.dto.mungple;

import com.delgo.reward.domain.mungple.Mungple;
import com.delgo.reward.mongoDomain.MungpleDetail;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class CafeDetailResDTO extends MungpleDetailResDTO {
    private final String residentDogName; // 상주견 이름
    private final String residentDogPhoto; // 상주견 프로필

    private final String representMenuTitle; // 대표 메뉴 제목
    private final List<String> representMenuPhotoUrls; // 대표 메뉴 사진 URL // ※무조건 3개 이상이어야 함.

    public CafeDetailResDTO(Mungple mungple, MungpleDetail mungpleDetail, int certCount){
        super(mungple,mungpleDetail,certCount);
        residentDogName = mungpleDetail.getResidentDogName();
        residentDogPhoto = mungpleDetail.getResidentDogPhoto();

        representMenuTitle = mungpleDetail.getRepresentMenuTitle();
        representMenuPhotoUrls = mungpleDetail.getRepresentMenuPhotoUrls();
    }
}
