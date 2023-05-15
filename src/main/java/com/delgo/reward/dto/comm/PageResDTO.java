package com.delgo.reward.dto.comm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class PageResDTO<T,F> {
    private int size;
    private int number;
    private boolean last;
    private List<T> content;

    public PageResDTO(List<T> data, Slice<F> slice) {
        this.content = data;
        size = slice.getSize();
        number = slice.getNumber();
        last = !slice.hasNext();
    }
}