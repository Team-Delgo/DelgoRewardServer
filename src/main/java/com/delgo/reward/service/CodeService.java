package com.delgo.reward.service;


import com.delgo.reward.domain.code.Code;
import com.delgo.reward.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;

    // Code 등록
    public void register(Code code){
        codeRepository.save(code);
    }

    // Code List 등록
    public void registerCodeList(List<Code> codeList) {
        codeRepository.saveAll(codeList);
    }

    // GeoCode 전체 조회
    public List<Code> getGeoCodeAll() {
        return codeRepository.findByType("geo");
    }

    // DogCode 전체 조회
    public List<Code> getBreedCodeAll() {
        return codeRepository.findByType("breed");
    }

    // SIGUGUNS [codeName]만으로 조회시 중복 발생 ex) 서울특별시 중구, 부산광역시 중구 중복의 경우 -> 서울특별시의 Code와 같이 조회
    public Code getGeoByAddress(String address) {
        String[] addressParts = address.split(" ");
        String sido = addressParts[0];
        String sigugun = addressParts[1];

        Code sidoCode = getGeoBySido(sido);
        return codeRepository.findBypCodeAndCodeName(sidoCode.getCode(), sigugun)
                .orElseThrow(() -> new NullPointerException("NOT FOUND GEOCODE  SIDO: " + sido + ", SIGUGUN : " + sigugun));
    }

    public Code getGeoBySido(String sido){
        String keyword = switch (sido) {
            case "제주특별자치도" -> "제주도";
            case "세종특별자치시" -> "세종특별시";
            default -> sido;
        };
        return codeRepository.findByCodeName(keyword)
                .orElseThrow(() -> new NullPointerException("NOT FOUND GEOCODE : " + keyword));
    }

    // Code 조회
    public Code getCode(String code) {
        return codeRepository.findByCode(code)
                .orElseThrow(() -> new NullPointerException("NOT FOUND CODE"));
    }

    // 주소 조회
    public String getAddress(String code, Boolean isSejong) {
        Code c = getCode(code); // 자식 GeoCode
        Code p = getCode(c.getPCode()); // 부모 GeoCode
        return (isSejong) ? p.getCodeName() : p.getCodeName() + " " + c.getCodeName();
    }

    // 품종 코드 중복 조회
    public boolean checkDuplicateBreedCode(String name) {
        return codeRepository.findByCodeName(name).isPresent();
    }
}
