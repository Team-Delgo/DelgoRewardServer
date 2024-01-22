package com.delgo.reward.user.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int smsId;
    private String phoneNo;
    private String randNum;
    private LocalDateTime authTime;

    public static SmsAuth from(String phoneNo, String randNum){
        return  SmsAuth.builder()
                .phoneNo(phoneNo)
                .randNum(randNum)
                .authTime(LocalDateTime.now())
                .build();
    }

    public SmsAuth update(String randNum) {
        return  SmsAuth.builder()
                .smsId(smsId)
                .phoneNo(phoneNo)
                .randNum(randNum)
                .authTime(LocalDateTime.now())
                .build();
    }

    public boolean isRandNumEqual(String enterNum){
        return randNum.equals(enterNum);
    }

    public boolean isAuthTimeValid() {
        return ChronoUnit.MINUTES.between(this.authTime, LocalDateTime.now()) < 10;
    }
}
