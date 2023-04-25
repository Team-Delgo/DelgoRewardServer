package com.delgo.reward.domain.user;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_count_id")
    private int categoryCountId;

    @Column(name = "user_Id")
    private int userId;

    @Column(name = "CA0001")
    private int CA0001;

    @Column(name = "CA0002")
    private int CA0002;

    @Column(name = "CA0003")
    private int CA0003;

    @Column(name = "CA0004")
    private int CA0004;

    @Column(name = "CA0005")
    private int CA0005;

    @Column(name = "CA0006")
    private int CA0006;

    @Column(name = "CA0007")
    private int CA0007;

    @Column(name = "CA9999")
    private int CA9999;

    public CategoryCount create(int userId){
        return CategoryCount.builder()
                .userId(userId)
                .build();
    }

    public CategoryCount addOne(String categoryCode){
        switch (categoryCode) {
            case "CA0001" -> {
                this.CA0001 += 1;
                return this;
            }
            case "CA0002" -> {
                this.CA0002 += 1;
                return this;
            }
            case "CA0003" -> {
                this.CA0003 += 1;
                return this;
            }
            case "CA0004" -> {
                this.CA0004 += 1;
                return this;
            }
            case "CA0005" -> {
                this.CA0005 += 1;
                return this;
            }
            case "CA0006" -> {
                this.CA0006 += 1;
                return this;
            }
            case "CA0007" -> {
                this.CA0007 += 1;
                return this;
            }
            case "CA9999" -> {
                this.CA9999 += 1;
                return this;
            }
            default -> {
                throw new NullPointerException("NOT FOUND categoryCode: " + categoryCode);
            }
        }
    }

    public CategoryCount minusOne(String categoryCode){
        switch (categoryCode) {
            case "CA0001" -> {
                if(this.CA0001 > 0)
                    this.CA0001 -= 1;
                return this;
            }
            case "CA0002" -> {
                if(this.CA0002 > 0)
                    this.CA0002 -= 1;
                return this;
            }
            case "CA0003" -> {
                if(this.CA0003 > 0)
                    this.CA0003 -= 1;
                return this;
            }
            case "CA0004" -> {
                if(this.CA0004 > 0)
                    this.CA0004 -= 1;
                return this;
            }
            case "CA0005" -> {
                if(this.CA0005 > 0)
                    this.CA0005 -= 1;
                return this;
            }
            case "CA0006" -> {
                if(this.CA0006 > 0)
                    this.CA0006 -= 1;
                return this;
            }
            case "CA0007" -> {
                if(this.CA0007 > 0)
                    this.CA0007 -= 1;
                return this;
            }
            case "CA9999" -> {
                if(this.CA9999 > 0)
                    this.CA9999 -= 1;
                return this;
            }
            default -> {
                throw new NullPointerException("NOT FOUND categoryCode: " + categoryCode);
            }
        }
    }

}
