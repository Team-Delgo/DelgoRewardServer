package com.delgo.reward.cert.response;

import com.delgo.reward.cert.domain.Certification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PageCertResponse {
    @Schema(description = "한 페이지에 보여줄 개수")
    private int size;
    @Schema(description = "현재 페이지 번호")
    private int number;
    @Schema(description = "마지막 페이지인지 여부")
    private boolean last;
    @Schema(description = "전체 개수")
    private long totalCount;
    @Schema(description = "조회 수(지도) /my, /other 에서만 동작")
    private long viewCount;
    @Schema(description = "데이터 리스트")
    private List<CertResponse> content;

    public static PageCertResponse from(Page<Certification> page, List<CertResponse> content) {
        return PageCertResponse.builder()
                .size(page.getSize())
                .number(page.getNumber())
                .last(page.isLast())
                .totalCount(page.getTotalElements())
                .content(content)
                .build();
    }

    public static PageCertResponse from(Page<Certification> page, List<CertResponse> content, int viewCount) {
        return PageCertResponse.builder()
                .size(page.getSize())
                .number(page.getNumber())
                .last(page.isLast())
                .totalCount(page.getTotalElements())
                .content(content)
                .viewCount(viewCount)
                .build();
    }
}