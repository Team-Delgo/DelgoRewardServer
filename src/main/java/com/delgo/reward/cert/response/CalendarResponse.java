package com.delgo.reward.cert.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CalendarResponse {
    @Schema(description = "날짜")
    private LocalDate date;
    @Schema(description = "인증 리스트")
    private List<CertResponse> dateList;

    public static List<CalendarResponse> from(List<CertResponse> certResponseList) {
        Map<LocalDate, List<CertResponse>> groupedByDate = certResponseList.stream()
                .collect(Collectors.groupingBy(cert -> LocalDate.from(cert.getRegistDt())));

        return groupedByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // 날짜 순으로 정렬
                .map(entry -> new CalendarResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
