package com.delgo.reward.dto.mungple.detail;

import com.delgo.reward.mongoDomain.mungple.Mungple;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class MungpleDetailByMenuResponse extends MungpleDetailResponse {
    private final String residentDogName; // 상주견 이름
    private final String residentDogPhoto; // 상주견 프로필

    private final String representMenuTitle; // 대표 메뉴 제목
    private final List<String> representMenuPhotoUrls; // 대표 메뉴 사진 URL // ※무조건 3개 이상이어야 함.

    public MungpleDetailByMenuResponse(Mungple mungple, int certCount, int bookmarkCount, boolean isBookmarked) {
        super(mungple, certCount, bookmarkCount, isBookmarked);
        residentDogName = mungple.getResidentDogName();
        residentDogPhoto = mungple.getResidentDogPhoto();

        representMenuTitle = mungple.getRepresentMenuTitle();

        List<String> representMenuBoardPhotoUrls = mungple.getRepresentMenuBoardPhotoUrls();
        List<String> representMenuPhotoUrls = mungple.getRepresentMenuPhotoUrls();
        if (representMenuBoardPhotoUrls == null || representMenuBoardPhotoUrls.isEmpty()) {
            this.representMenuPhotoUrls = representMenuPhotoUrls;
        } else {
            representMenuBoardPhotoUrls.addAll(representMenuPhotoUrls);
            this.representMenuPhotoUrls = representMenuBoardPhotoUrls;
        }
    }
}
