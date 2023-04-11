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
    private int CA0009;

    @Column(name = "CA9999")
    private int CA9999;
}
