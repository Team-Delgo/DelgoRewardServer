package com.delgo.reward.dto.map;

import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.cert.CertByMungpleResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OtherMapDTO {
    private int userId;
    private String nickname;
    private int viewCount;

    List<CertByMungpleResDTO> certs;
    int totalCount;


    public OtherMapDTO(User user, List<CertByMungpleResDTO> certs, int totalCount){
        userId = user.getUserId();
        nickname = user.getName();
        viewCount = user.getViewCount();

        this.certs = certs;
        this.totalCount = totalCount;
    }
}