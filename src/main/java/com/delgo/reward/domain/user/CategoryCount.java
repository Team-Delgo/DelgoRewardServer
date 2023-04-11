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

    public CategoryCount addOne(String categoryCode){
        switch (categoryCode) {
            case "CA0001" -> {
                return addCA0001();
            }
            case "CA0002" -> {
                return addCA0002();
            }
            case "CA0003" -> {
                return addCA0003();
            }
            case "CA0004" -> {
                return addCA0004();
            }
            case "CA0005" -> {
                return addCA0005();
            }
            case "CA0006" -> {
                return addCA0006();
            }
            case "CA0007" -> {
                return addCA0007();
            }
            case "CA9999" -> {
                return addCA9999();
            }
            default -> {
                throw new NullPointerException("NOT FOUND categoryCode: " + categoryCode);
            }
        }
    }

    public CategoryCount addCA0001(){
        this.CA0001 += 1;
        return this;
    }

    public CategoryCount addCA0002(){
        this.CA0002 += 1;
        return this;
    }

    public CategoryCount addCA0003(){
        this.CA0003 += 1;
        return this;
    }

    public CategoryCount addCA0004(){
        this.CA0004 += 1;
        return this;
    }

    public CategoryCount addCA0005(){
        this.CA0005 += 1;
        return this;
    }

    public CategoryCount addCA0006(){
        this.CA0006 += 1;
        return this;
    }

    public CategoryCount addCA0007(){
        this.CA0007 += 1;
        return this;
    }

    public CategoryCount addCA9999(){
        this.CA9999 += 1;
        return this;
    }
}
