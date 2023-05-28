package com.delgo.reward.dto.mungple;

import com.delgo.reward.comm.code.DetailCode;
import com.delgo.reward.domain.Mungple;
import com.delgo.reward.mongoDomain.MungpleDetailData;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class MungpleDetailResDTO extends MungpleResDTO {
    private int mungpleId;
    private Map<String, String> businessHour;
    private String residentDogName;
    private String representSite;
    private Map<String, String> representMenuTitle;
    private Map<String, DetailCode> acceptSize;
    private String enterDesc;
    private List<String> representMenuPhotoUrl;
    private int parkingLimit;
    private String editorNoteUrl;
    private String copyLink;



    public MungpleDetailResDTO(Mungple mungple, MungpleDetailData mungpleDetailData){
        super(mungple);

        this.mungpleId = mungpleDetailData.getMungpleId();
        this.businessHour = mungpleDetailData.getBusinessHour();
        this.residentDogName = mungpleDetailData.getResidentDogName();
        this.representSite = mungpleDetailData.getRepresentSite();
        this.representMenuTitle =mungpleDetailData.getRepresentMenuTitle();
        this.acceptSize = mungpleDetailData.getAcceptSize();
        this.enterDesc = mungpleDetailData.getEnterDesc();
        this.representMenuPhotoUrl = mungpleDetailData.getRepresentMenuPhotoUrl();
        this.parkingLimit = mungpleDetailData.getParkingLimit();
        this.editorNoteUrl = mungpleDetailData.getEditorNoteUrl();
        this.copyLink = mungpleDetailData.getCopyLink();
    }
}
