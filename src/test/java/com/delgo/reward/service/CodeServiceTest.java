package com.delgo.reward.service;

import com.delgo.reward.code.service.CodeService;
import com.delgo.reward.comm.code.CodeType;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.code.domain.Code;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CodeServiceTest {
    @Autowired
    CodeService codeService;

    @Test
    void getListByType_breed() {
        // given
        CodeType type = CodeType.breed;

        // when
        List<Code> codeList = codeService.getListByType(type);

        // then
        assertThat(codeList.size()).isGreaterThan(0);
        assertThat(codeList).extracting(Code::getType).containsOnly(type);
    }

    @Test
    void getListByType_geo() {
        // given
        CodeType type = CodeType.geo;

        // when
        List<Code> codeList = codeService.getListByType(type);

        // then
        assertThat(codeList.size()).isGreaterThan(0);
        assertThat(codeList).extracting(Code::getType).containsOnly(type);
    }

    @Test
    void getOneByCode() {
        // given
        String code = "101140";

        // when
        Code selectedCode = codeService.getOneByCode(code);

        // then
        assertThat(selectedCode.getCode()).isEqualTo(code);
    }

    @Test
    void getOneByCode_예외처리() {
        // given
        String code = "109999";

        // when

        // then
        assertThatThrownBy(() -> codeService.getOneByCode(code))
                .isInstanceOf(NotFoundDataException.class);
    }

    @Test
    void getOneByCodeName() {
        // given
        String codeName = "서울특별시";

        // when
        Code code = codeService.getOneByCodeName(codeName);

        // then
        assertThat(code.getCodeName()).isEqualTo(codeName);
    }

    @Test
    void getOneByCodeName_예외처리() {
        // given
        String codeName = "이상한 시이름";

        // when

        // then
        assertThatThrownBy(() -> codeService.getOneByCodeName(codeName))
                .isInstanceOf(NotFoundDataException.class);
    }

    @Test
    void getOneByPCodeAndCodeName() {
        // given
        String pCode = "101000";
        String codeName = "마포구";

        // when
        Code code = codeService.getOneByPCodeAndCodeName(pCode, codeName);

        // then
        assertThat(code.getPCode()).isEqualTo(pCode);
        assertThat(code.getCodeName()).isEqualTo(codeName);
    }

    @Test
    void getOneByPCodeAndCodeName_예외처리() {
        // given
        String pCode = "109999";
        String codeName = "마포구";

        // when

        // then
        assertThatThrownBy(() -> codeService.getOneByPCodeAndCodeName(pCode, codeName))
                .isInstanceOf(NotFoundDataException.class);
    }

    @Test
    void getGeoCodeByAddress() {
        // given
        String address = "서울특별시 송파구 송파동 54-13";

        // when
        Code code = codeService.getGeoCodeByAddress(address);
        Code pCode = codeService.getOneByCode(code.getPCode());

        // then
        assertThat(code.getCodeName()).isEqualTo("송파구");
        assertThat(pCode.getCodeName()).isEqualTo("서울특별시");
    }

    @Test
    void getAddressByGeoCode() {
        // given
        String geoCode = "101140";

        // when
        String address = codeService.getAddressByGeoCode(geoCode);

        // then
        String expectedAddress = "서울특별시 서대문구";
        assertThat(address).isEqualTo(expectedAddress);
    }

    @Test
    void getAddressByGeoCode_세종특별시() {
        // given
        String geoCode = "118000";

        // when
        String address = codeService.getAddressByGeoCode(geoCode);

        // then
        String expectedAddress = "세종특별시";
        assertThat(address).isEqualTo(expectedAddress);
    }
}