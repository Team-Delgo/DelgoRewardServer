package com.delgo.reward.dto.comm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class PageResDTO<T> {
    @Schema(description = "한 페이지에 보여줄 개수")
    private int size;
    @Schema(description = "현재 페이지 번호")
    private int number;
    @Schema(description = "마지막 페이지인지 여부")
    private boolean last;
    @Schema(description = "전체 개수")
    private int totalCount;
    @Schema(description = "데이터 리스트")
    private List<T> content;

    public PageResDTO(List<T> data, int size, int number, boolean last) {
        this.content = data;
        this.size = size;
        this.number = number;
        this.last = last;
    }

    // [/certification/my] , [certification/other] 의 경우 totalCount가 필요함.
    public PageResDTO(List<T> data, int size, int number, boolean last, int totalCount) {
        this.content = data;
        this.size = size;
        this.number = number;
        this.last = last;
        this.totalCount = totalCount;
    }
}