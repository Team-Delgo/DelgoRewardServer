package com.delgo.reward.certification.controller.response;

import com.delgo.reward.certification.controller.port.CertPhotoService;
import com.delgo.reward.certification.controller.port.ReactionService;
import com.delgo.reward.certification.domain.CertPhoto;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.domain.Reaction;
import com.delgo.reward.dto.comm.PageCustom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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

    public static PageCertResponse from(Integer ownerId, PageCustom<Certification> page,
                                        CertPhotoService certPhotoService, ReactionService reactionService) {
        Map<Integer, List<CertPhoto>> photoMap = certPhotoService.getMapByCertList(page.getContent());
        Map<Integer, List<Reaction>> reactionMap = reactionService.getMapByCertList(page.getContent());

        List<CertResponse> content = page.getContent().stream().map(cert -> {
            List<CertPhoto> photoList = photoMap.get(cert.getCertificationId());
            List<Reaction> reactionList = reactionMap.get(cert.getCertificationId());

            return CertResponse.from(ownerId, cert, photoList, reactionList);
        }).toList();

        return PageCertResponse.builder().
                size(page.getSize())
                .number(page.getNumber())
                .last(page.isLast())
                .totalCount(page.getTotalCount())
                .content(content)
                .build();
    }

    public PageCertResponse setViewCount(Integer viewCount) {
        this.viewCount = viewCount;

        return this;
    }
}