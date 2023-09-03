package com.delgo.reward.dto.comm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class PageResDTO<T> {
    private int size;
    private int number;
    private boolean last;
    private List<T> content;

    private int totalCount;

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