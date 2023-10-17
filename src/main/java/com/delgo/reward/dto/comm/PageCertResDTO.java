package com.delgo.reward.dto.comm;

import com.delgo.reward.dto.cert.CertResDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageCertResDTO extends PageResDTO<CertResDTO> {
    @Schema(description = "조회 수")
    private int viewCount;

    public PageCertResDTO(List<CertResDTO> data, int size, int number, boolean last) {
        super(data, size, number, last);
    }

    public PageCertResDTO(List<CertResDTO> data, int size, int number, boolean last, int totalCount, int viewCount) {
        super(data, size, number, last, totalCount);
        this.viewCount = viewCount;
    }
}