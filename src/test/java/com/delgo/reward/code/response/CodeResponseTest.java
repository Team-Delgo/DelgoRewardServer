package com.delgo.reward.code.response;

import com.delgo.reward.code.domain.Code;
import com.delgo.reward.code.service.CodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CodeResponseTest {
    @Autowired
    CodeService codeService;
    
    @Test
    void from() {
        // given
        Code code = codeService.getOneByCode("101000");

        // when
        CodeResponse codeResponse = CodeResponse.from(code);

        // then
        assertThat(codeResponse.getCode()).isEqualTo(code.getCode());
        assertThat(codeResponse.getPCode()).isEqualTo(code.getPCode());
        assertThat(codeResponse.getCodeName()).isEqualTo(code.getCodeName());
        assertThat(codeResponse.getCodeDesc()).isEqualTo(code.getCodeDesc());
        assertThat(codeResponse.getN_code()).isEqualTo(Integer.parseInt(code.getCode()));
        assertThat(codeResponse.getN_pCode()).isEqualTo(Integer.parseInt(code.getPCode()));
    }

    @Test
    void fromList() {
        // given
        Code code1 = codeService.getOneByCode("101000");
        Code code2 = codeService.getOneByCode("102000");
        List<Code> codeList = List.of(code1, code2);

        // when
        List<CodeResponse> codeResposneList = CodeResponse.fromList(codeList);

        // then
        assertThat(codeResposneList.get(0).getCode()).isEqualTo(codeList.get(0).getCode());
        assertThat(codeResposneList.get(0).getPCode()).isEqualTo(codeList.get(0).getPCode());
        assertThat(codeResposneList.get(0).getCodeName()).isEqualTo(codeList.get(0).getCodeName());
        assertThat(codeResposneList.get(0).getCodeDesc()).isEqualTo(codeList.get(0).getCodeDesc());
        assertThat(codeResposneList.get(0).getN_code()).isEqualTo(Integer.parseInt(codeList.get(0).getCode()));
        assertThat(codeResposneList.get(0).getN_pCode()).isEqualTo(Integer.parseInt(codeList.get(0).getPCode()));

        assertThat(codeResposneList.get(1).getCode()).isEqualTo(codeList.get(1).getCode());
        assertThat(codeResposneList.get(1).getPCode()).isEqualTo(codeList.get(1).getPCode());
        assertThat(codeResposneList.get(1).getCodeName()).isEqualTo(codeList.get(1).getCodeName());
        assertThat(codeResposneList.get(1).getCodeDesc()).isEqualTo(codeList.get(1).getCodeDesc());
        assertThat(codeResposneList.get(1).getN_code()).isEqualTo(Integer.parseInt(codeList.get(1).getCode()));
        assertThat(codeResposneList.get(1).getN_pCode()).isEqualTo(Integer.parseInt(codeList.get(1).getPCode()));
    }
}