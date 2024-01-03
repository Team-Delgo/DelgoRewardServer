package com.delgo.reward.cert.response;


import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.domain.Reaction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CalendarResponse {
    @Schema(description = "날짜")
    private LocalDate date;
    @Schema(description = "인증 리스트")
    private List<CertResponse> dateList;

    public static List<CalendarResponse> from(int userId, Page<Certification> page, Map<Integer, List<Reaction>> reactionMap) {
        // 날짜 별로 그룹화
        Map<LocalDate, List<CertResponse>> groupedByDate = CertResponse.fromList(userId, page.getContent(), reactionMap).stream()
                .collect(Collectors.groupingBy(cert -> LocalDate.from(cert.getRegistDt())));

        // 각 그룹에 대해 CalendarResponse 객체 생성
        return groupedByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // 날짜 순으로 정렬
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<CertResponse> dateList = entry.getValue();
                    return new CalendarResponse(date, dateList);
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CalendarResponse that = (CalendarResponse) obj;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}