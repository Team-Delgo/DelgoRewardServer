package com.delgo.reward.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentDTO {
    @NotNull
    private Integer userId;
    @NotNull
    private Integer commentId;
    @NotNull
    private Integer certificationId;
    @NotNull
    private Boolean isReply;
    @NotNull
    private String content;
    @NotNull
    private String userName;
    @NotNull
    private String profile;
    @NotNull
    @JsonFormat(pattern="yyyy.MM.dd/HH:mm/E")
    @CreationTimestamp
    private LocalDateTime createDt;
}
