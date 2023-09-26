package com.delgo.reward.dto.comm;

import com.delgo.reward.dto.cert.CertResDTO;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageCertResDTO extends PageResDTO<CertResDTO> {
    public PageCertResDTO(List<CertResDTO> data, int size, int number, boolean last) {
        super(data, size, number, last);
    }

    public PageCertResDTO(List<CertResDTO> data, int size, int number, boolean last, int totalCount) {
        super(data, size, number, last, totalCount);
    }
}