package com.delgo.reward.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class SignUpDTO {
    @NotBlank
    private String userName;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String phoneNo;
    @NotBlank
    private String geoCode;
    @NotBlank
    private String pGeoCode;
    @NotBlank
    private String petName;
    @NotNull
    private String breed;
    @NotNull
    private LocalDate birthday;
}
