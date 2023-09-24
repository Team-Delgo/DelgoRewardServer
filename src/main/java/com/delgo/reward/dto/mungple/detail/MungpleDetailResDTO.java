package com.delgo.reward.dto.mungple.detail;

import com.delgo.reward.comm.code.BusinessHourCode;
import com.delgo.reward.comm.code.DetailCode;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
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
    @Schema(description = "허용 크기 설정 key:[S, M, L] value: DetailCode ", implementation = DetailCode.class)
    protected Map<String, DetailCode> acceptSize;
    @Schema(description = "운영 시간 (요일 별로 표시)")
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
    @Schema(description = "유저가 저장했는지 여부")
    protected Boolean isBookmarked;

    public MungpleDetailResDTO(MongoMungple mongoMungple, int certCount, int bookmarkCount, boolean isBookmarked){
        super(mongoMungple, certCount, bookmarkCount);

        this.phoneNo = mongoMungple.getPhoneNo();
        this.enterDesc = mongoMungple.getEnterDesc();
        this.acceptSize = mongoMungple.getAcceptSize();
        this.businessHour = mongoMungple.getBusinessHour();
        this.instaId = mongoMungple.getInstaId();
        this.photoUrls = mongoMungple.getPhotoUrls();
        this.editorNoteUrl = mongoMungple.getDetailUrl();
        this.isParking = mongoMungple.getIsParking();
        this.parkingInfo = mongoMungple.getParkingInfo();
        this.isBookmarked = isBookmarked;
    }
}
