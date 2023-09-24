package com.delgo.reward.dto.user;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.user.CategoryCount;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import lombok.*;

import java.util.List;
import java.util.Map;


@Getter
@ToString
@NoArgsConstructor
public class UserByCertCountResDTO extends UserResDTO {

    private int totalCount;
    private int totalCountByMungple;
    private int CA0001Count;
    private int CA0002Count;
    private int CA0003Count;
    private int CA0004Count;
    private int CA0005Count;
    private int CA0006Count;
    private int CA0007Count;
    private int CA9999Count;

    // Activity
    private Map<CategoryCode, Integer> activityMapByCategoryCode;
    // 가장 많이 방문 한 멍플(최대 3개)
    private List<MongoMungple> top3VisitedMungpleList;

    public UserByCertCountResDTO(User user, int totalCount, int totalCountByMungple, CategoryCount categoryCount, Map<CategoryCode, Integer> activityMapByCategoryCode, List<MongoMungple> top3VisitedMungpleList) {
        super(user);
        this.totalCount = totalCount;
        this.totalCountByMungple = totalCountByMungple;
        CA0001Count = categoryCount.getCA0001();
        CA0002Count = categoryCount.getCA0002();
        CA0003Count = categoryCount.getCA0003();
        CA0004Count = categoryCount.getCA0004();
        CA0005Count = categoryCount.getCA0005();
        CA0006Count = categoryCount.getCA0006();
        CA0007Count = categoryCount.getCA0007();
        CA9999Count = categoryCount.getCA9999();
        this.activityMapByCategoryCode = activityMapByCategoryCode;
        this.top3VisitedMungpleList = top3VisitedMungpleList;
    }
}