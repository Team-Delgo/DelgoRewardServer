package com.delgo.reward.user.infrastructure.entity;

import com.delgo.reward.user.domain.CategoryCount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category_count")
public class CategoryCountEntity {
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

    public CategoryCount toModel(){
        return CategoryCount
                .builder()
                .categoryCountId(categoryCountId)
                .userId(userId)
                .CA0001(CA0001)
                .CA0002(CA0002)
                .CA0003(CA0003)
                .CA0004(CA0004)
                .CA0005(CA0005)
                .CA0006(CA0006)
                .CA0007(CA0007)
                .CA9999(CA9999)
                .build();
    }

    public static CategoryCountEntity from(CategoryCount categoryCount){
        return CategoryCountEntity
                .builder()
                .categoryCountId(categoryCount.getCategoryCountId())
                .userId(categoryCount.getUserId())
                .CA0001(categoryCount.getCA0001())
                .CA0002(categoryCount.getCA0002())
                .CA0003(categoryCount.getCA0003())
                .CA0004(categoryCount.getCA0004())
                .CA0005(categoryCount.getCA0005())
                .CA0006(categoryCount.getCA0006())
                .CA0007(categoryCount.getCA0007())
                .CA9999(categoryCount.getCA9999())
                .build();
    }

}
