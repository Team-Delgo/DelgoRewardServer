package com.delgo.reward.dto.user;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.user.User;
import lombok.*;

import java.util.List;
import java.util.Map;


@Getter
@ToString
@NoArgsConstructor
public class AccountResDTO extends UserResDTO {
    private int totalCount;
    private int totalCountByMungple;

    // Activity
    private Map<CategoryCode, Integer> activityMapByCategoryCode;
    // 가장 많이 방문 한 멍플(최대 3개)
    private List<UserVisitMungpleCountDTO> top3VisitedMungpleList;

    public AccountResDTO(User user, int totalCount, int totalCountByMungple, Map<CategoryCode, Integer> activityMapByCategoryCode, List<UserVisitMungpleCountDTO> top3VisitedMungpleList) {
        super(user);
        this.totalCount = totalCount;
        this.totalCountByMungple = totalCountByMungple;
        this.activityMapByCategoryCode = activityMapByCategoryCode;
        this.top3VisitedMungpleList = top3VisitedMungpleList;
    }
}