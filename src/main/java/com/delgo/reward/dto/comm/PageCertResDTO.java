package com.delgo.reward.dto.comm;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.cert.CertResDTO;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageCertResDTO extends PageResDTO<CertResDTO> {
    public PageCertResDTO(PageResDTO<Certification> page, int userId) {
        super(page.getContent().stream().map(cert -> new CertResDTO(cert, userId)).toList(),
                page.getSize(),
                page.getNumber(),
                page.isLast(),
                page.getTotalCount());
    }
}