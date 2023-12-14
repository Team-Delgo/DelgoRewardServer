package com.delgo.reward.dto.mungple.detail;

import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.DetailCode;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@ToString
public class MungpleDetailResDTO extends MungpleResDTO {
    @Schema(description = "매장 번호")
    protected String phoneNo;
    @Schema(description = "강아지 동반 안내 매장 설명문")
    protected String enterDesc;
    @Schema(description = "허용 크기 설정 | key:[S, M, L] | value: DetailCode ", implementation = DetailCode.class)
    protected Map<String, DetailCode> acceptSize;
    @Schema(description = "운영 시간 | key: BusinessHourCode | value: 입력 값", implementation = BusinessHourCode.class)
    protected Map<BusinessHourCode, String> businessHour;
    @Schema(description = "Insta Id")
    protected String instaId;
    @Schema(description = "매장 사진 URL List")
    protected List<String> photoUrls;
    @Schema(description = "에디터 메모 URL")
    protected String editorNoteUrl;
    @Schema(description = "주차 가능 여부")
    protected Boolean isParking;
    @Schema(description = "주차 정보")
    protected String parkingInfo;

    public MungpleDetailResDTO(Mungple mungple, int certCount, int bookmarkCount, boolean isBookmarked){
        super(mungple, certCount, bookmarkCount, isBookmarked);

        this.phoneNo = mungple.getPhoneNo();
        this.enterDesc = mungple.getEnterDesc();
        this.acceptSize = mungple.getAcceptSize();
        this.businessHour = mungple.getBusinessHour();
        this.instaId = mungple.getInstaId();
        this.photoUrls = mungple.getPhotoUrls();
        this.editorNoteUrl = mungple.getDetailUrl();
        this.isParking = mungple.getIsParking();
        this.parkingInfo = mungple.getParkingInfo();
    }
}
