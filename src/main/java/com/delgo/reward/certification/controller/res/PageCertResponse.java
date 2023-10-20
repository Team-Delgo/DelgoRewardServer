package com.delgo.reward.certification.controller.res;

import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.dto.comm.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageCertResponse {
    @Schema(description = "한 페이지에 보여줄 개수")
    private int size;
    @Schema(description = "현재 페이지 번호")
    private int number;
    @Schema(description = "마지막 페이지인지 여부")
    private boolean last;
    @Schema(description = "전체 개수")
    private long totalCount;
    @Schema(description = "데이터 리스트")
    private List<CertResponse> content;

    public PageCertResponse(Page<Certification> page, int userId) {
        this.content = page.getContent().stream().map(cert -> new CertResponse(cert, userId)).toList();
        this.size = page.getSize();
        this.number = page.getNumber();
        this.last = page.isLast();
        this.totalCount = page.getTotalCount();
    }
}