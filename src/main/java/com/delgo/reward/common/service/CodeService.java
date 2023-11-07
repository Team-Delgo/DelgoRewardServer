package com.delgo.reward.common.service;


import com.delgo.reward.common.domain.Code;
import com.delgo.reward.common.domain.CodeCondition;
import com.delgo.reward.common.domain.CodeType;
import com.delgo.reward.common.service.port.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CodeService {
    private final CodeRepository codeRepository;

    public List<Code> getListByType(CodeType type) {
        return codeRepository.findListByType(type);
    }

    public Code getOneByCondition(CodeCondition condition){
        return codeRepository.findOneByCondition(condition);
    }

    // SIGUGUNS [codeName]만으로 조회시 중복 발생 ex) 서울특별시 중구, 부산광역시 중구 중복의 경우 -> 서울특별시의 Code와 같이 조회
    public Code getGeoCodeByAddress(String address) {
        String SIDO = extractSIDO(address);
        String SIGUGUN = extractSIGUGUN(address);

        String pCode = getOneByCondition(CodeCondition.byCodeName(SIDO)).getCode();
        return getOneByCondition(CodeCondition.byPCodeAndCodeName(pCode, SIGUGUN));
    }

    public String getAddressByGeoCode(String code, Boolean isSejong) {
        Code c = getOneByCondition(CodeCondition.byCode(code)); // 자식 GeoCode
        Code p = getOneByCondition(CodeCondition.byCode(c.getCode())); // 부모 GeoCode
        return (isSejong) ? p.getCodeName() : p.getCodeName() + " " + c.getCodeName();
    }

    private String extractSIDO(String address){
        String[] addressParts = address.split(" ");
        return switch (addressParts[0]) {
            case "제주특별자치도" -> "제주도";
            case "세종특별자치시" -> "세종특별시";
            default -> addressParts[0];
        };
    }

    private String extractSIGUGUN(String address){
        String[] addressParts = address.split(" ");
        return addressParts[1];
    }
}
