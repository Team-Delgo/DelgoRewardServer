package com.delgo.reward.dto.user;

import com.delgo.reward.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PageUserResponse {
    @Schema(description = "한 페이지에 보여줄 개수")
    private int size;
    @Schema(description = "현재 페이지 번호")
    private int number;
    @Schema(description = "마지막 페이지인지 여부")
    private boolean last;
    @Schema(description = "전체 개수")
    private long totalCount;
    @Schema(description = "데이터 리스트")
    private List<UserResponse> content;

    public static PageUserResponse from(Page<User> page) {
        return PageUserResponse.builder().
                size(page.getSize())
                .number(page.getNumber())
                .last(page.isLast())
                .totalCount(page.getTotalElements())
                .content(UserResponse.fromSearchList(page.getContent()))
                .build();
    }
}