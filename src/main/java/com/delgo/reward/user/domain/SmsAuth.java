package com.delgo.reward.user.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SmsAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sms_id")
    private int smsId;

    @Column(name = "rand_num")
    private String randNum;

    @CreationTimestamp
    @Column(name = "auth_time")
    private LocalDateTime authTime;

    @Column(name = "phone_no")
    private String phoneNo;

    public SmsAuth setRandNum(String randNum) {
        this.randNum = randNum;
        this.authTime = LocalDateTime.now();

        return this;
    }
}
