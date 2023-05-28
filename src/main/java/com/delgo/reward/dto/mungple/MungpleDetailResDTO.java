package com.delgo.reward.dto.mungple;

import com.delgo.reward.domain.Mungple;
import com.delgo.reward.mongoDomain.MungpleDetailData;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalTime;
import java.util.Map;

@Getter
@ToString
public class MungpleDetailResDTO extends MungpleResDTO {
    private int mungpleId;
    private String address;
    private Map<String, LocalTime> businessHour;
    private String telNumber;
    private boolean isResidentDog;
    private Map<String, Boolean> restriction;
    private Map<String, Boolean> acceptSize;
    private Map<Integer, String> representMenuTitle;
    private Map<Integer, String> representMenuPhotoUrl;
    private boolean isParking;
    private Map<String, String> parkingInfo;


    public MungpleDetailResDTO(Mungple mungple, MungpleDetailData mungpleDetailData){
        super(mungple);

        mungpleId = mungple.getMungpleId();
        this.address = mungpleDetailData.getAddress();
        this.businessHour = mungpleDetailData.getBusinessHour();
        this.telNumber = mungpleDetailData.getTelNumber();
        this.isResidentDog = mungpleDetailData.isResidentDog();
        this.restriction = mungpleDetailData.getRestriction();
        this.acceptSize = mungpleDetailData.getAcceptSize();
        this.representMenuTitle = mungpleDetailData.getRepresentMenuTitle();
        this.representMenuPhotoUrl = mungpleDetailData.getRepresentMenuPhotoUrl();
        this.isParking = mungpleDetailData.isParking();
        this.parkingInfo = mungpleDetailData.getParkingInfo();
    }
}
