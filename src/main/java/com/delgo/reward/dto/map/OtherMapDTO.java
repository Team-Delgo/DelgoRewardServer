package com.delgo.reward.dto.map;

import com.delgo.reward.user.domain.User;
import com.delgo.reward.cert.response.CertResponse;
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

    List<CertResponse> certs;
    long totalCount;


    public OtherMapDTO(User user, List<CertResponse> certs, long totalCount){
        userId = user.getUserId();
        nickname = user.getName();
        viewCount = user.getViewCount();

        this.certs = certs;
        this.totalCount = totalCount;
    }
}