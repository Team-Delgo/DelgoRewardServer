package com.delgo.reward.dto.cert;

import com.delgo.reward.domain.certification.CertPhoto;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.certification.Reaction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

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

    public static PageCertResponse from(Integer userId, Page<Certification> page, Map<Integer, List<Reaction>> reactionMap, Map<Integer, List<CertPhoto>> photoMap) {
        List<CertResponse> content = CertResponse.fromList(userId, page.getContent(), reactionMap, photoMap);
        return PageCertResponse.builder().
                size(page.getSize())
                .number(page.getNumber())
                .last(page.isLast())
                .totalCount(page.getTotalElements())
                .content(content)
                .build();
    }

    public PageCertResponse setViewCount(Integer viewCount) {
        this.viewCount = viewCount;

        return this;
    }
}