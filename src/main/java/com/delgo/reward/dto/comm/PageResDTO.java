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

    public PageResDTO(List<T> data, int size, int number, boolean last) {
        this.content = data;
        this.size = size;
        this.number = number;
        this.last = last;
    }
}