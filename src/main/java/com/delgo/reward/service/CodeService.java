package com.delgo.reward.service;


import com.delgo.reward.comm.code.CodeType;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.domain.code.Code;
import com.delgo.reward.repository.CodeRepository;
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

    public Code getOneByCode(String code) {
        return codeRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundDataException("[Code] code : " + code));
    }

    public Code getOneByCodeName(String codeName) {
        return codeRepository.findByCodeName(codeName)
                .orElseThrow(() -> new NotFoundDataException("[Code] codeName : " + codeName));
    }

    public Code getOneByPCodeAndCodeName(String pCode, String codeName) {
        return codeRepository.findBypCodeAndCodeName(pCode, codeName)
                .orElseThrow(() -> new NotFoundDataException("[Code] pCode : " + pCode + " codeName : " + codeName));
    }

    // SIGUGUNS [codeName]만으로 조회시 중복 발생 ex) 서울특별시 중구, 부산광역시 중구 중복의 경우 -> 서울특별시의 Code와 같이 조회
    public Code getGeoCodeByAddress(String address) {
        String SIDO = extractSIDO(address);
        String SIGUGUN = extractSIGUGUN(address);

        String pCode = getOneByCodeName(SIDO).getCode();
        return getOneByPCodeAndCodeName(pCode, SIGUGUN);
    }

    public String getAddressByGeoCode(String code) {
        Code childCode = getOneByCode(code); // 자식 GeoCode
        if ("세종특별시".equals(childCode.getCodeName())) {
            return childCode.getCodeName();
        }

        Code parentCode = getOneByCode(childCode.getPCode()); // 부모 GeoCode
        return parentCode.getCodeName() + " " + childCode.getCodeName();
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
