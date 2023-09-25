package com.delgo.reward.dto.comm;

import com.delgo.reward.dto.cert.CertByMungpleResDTO;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageCertByMungpleResDTO extends PageResDTO<CertByMungpleResDTO> {
    public PageCertByMungpleResDTO(List<CertByMungpleResDTO> data, int size, int number, boolean last) {
        super(data, size, number, last);
    }
}