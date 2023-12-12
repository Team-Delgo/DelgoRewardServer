package com.delgo.reward.dto.comm;

import com.delgo.reward.dto.cert.CertResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class PageCertResponse extends PageResponse<CertResponse> {
    @Schema(description = "조회 수")
    private int viewCount;

    public PageCertResponse(List<CertResponse> data, int size, int number, boolean last) {
        super(data, size, number, last);
    }

    public PageCertResponse(List<CertResponse> data, int size, int number, boolean last, int totalCount, int viewCount) {
        super(data, size, number, last, totalCount);
        this.viewCount = viewCount;
    }
}