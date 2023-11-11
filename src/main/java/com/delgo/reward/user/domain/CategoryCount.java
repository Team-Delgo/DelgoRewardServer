package com.delgo.reward.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CategoryCount {
    private Integer categoryCountId;

    private Integer userId;
    private Integer CA0001;
    private Integer CA0002;
    private Integer CA0003;
    private Integer CA0004;
    private Integer CA0005;
    private Integer CA0006;
    private Integer CA0007;
    private Integer CA9999;

    public static CategoryCount create(int userId){
        return CategoryCount.builder()
                .userId(userId)
                .build();
    }

    public void addOne(String categoryCode){
        switch (categoryCode) {
            case "CA0001" -> {
                this.CA0001 += 1;
            }
            case "CA0002" -> {
                this.CA0002 += 1;
            }
            case "CA0003" -> {
                this.CA0003 += 1;
            }
            case "CA0004" -> {
                this.CA0004 += 1;
            }
            case "CA0005" -> {
                this.CA0005 += 1;
            }
            case "CA0006" -> {
                this.CA0006 += 1;
            }
            case "CA0007" -> {
                this.CA0007 += 1;
            }
            case "CA9999" -> {
                this.CA9999 += 1;
            }
            default -> {
                throw new NullPointerException("NOT FOUND categoryCode: " + categoryCode);
            }
        }
    }

    public void minusOne(String categoryCode){
        switch (categoryCode) {
            case "CA0001" -> {
                if(this.CA0001 > 0)
                    this.CA0001 -= 1;
            }
            case "CA0002" -> {
                if(this.CA0002 > 0)
                    this.CA0002 -= 1;
            }
            case "CA0003" -> {
                if(this.CA0003 > 0)
                    this.CA0003 -= 1;
            }
            case "CA0004" -> {
                if(this.CA0004 > 0)
                    this.CA0004 -= 1;
            }
            case "CA0005" -> {
                if(this.CA0005 > 0)
                    this.CA0005 -= 1;
            }
            case "CA0006" -> {
                if(this.CA0006 > 0)
                    this.CA0006 -= 1;
            }
            case "CA0007" -> {
                if(this.CA0007 > 0)
                    this.CA0007 -= 1;
            }
            case "CA9999" -> {
                if(this.CA9999 > 0)
                    this.CA9999 -= 1;
            }
            default -> {
                throw new NullPointerException("NOT FOUND categoryCode: " + categoryCode);
            }
        }
    }
}
