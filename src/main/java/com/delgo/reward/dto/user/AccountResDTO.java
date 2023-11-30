package com.delgo.reward.dto.user;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.Map;


@Getter
@ToString
@NoArgsConstructor
public class AccountResDTO extends UserResDTO {
    @Schema(description = "활동 비율 표시 [Key: CategoryCode(ENUM)]")
    private Map<CategoryCode, Integer> activityMapByCategoryCode;
    @Schema(description = "가장 많이 방문 한 멍플(최대 3개)")
    private List<VisitCountDTO> top3VisitedMungpleList;

    public AccountResDTO(User user, Map<CategoryCode, Integer> activityMapByCategoryCode, List<VisitCountDTO> top3VisitedMungpleList) {
        super(user);
        this.activityMapByCategoryCode = activityMapByCategoryCode;
        this.top3VisitedMungpleList = top3VisitedMungpleList;
    }
}