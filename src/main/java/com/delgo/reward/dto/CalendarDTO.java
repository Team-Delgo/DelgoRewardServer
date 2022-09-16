package com.delgo.reward.dto;

import com.delgo.reward.domain.Certification;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDTO {
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
    private List<Certification> dateList;
}
