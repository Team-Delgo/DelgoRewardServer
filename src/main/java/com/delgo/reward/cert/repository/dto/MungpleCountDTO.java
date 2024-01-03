package com.delgo.reward.cert.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MungpleCountDTO {
    private int mungpleId; // 멍플 고유 아이디
    private int count; // 개수


    public MungpleCountDTO(Integer mungpleId, Long count) {
        this.mungpleId = mungpleId;
        this.count = count.intValue(); // 형 변환
    }
}