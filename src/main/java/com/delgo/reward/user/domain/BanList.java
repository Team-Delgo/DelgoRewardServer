package com.delgo.reward.user.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BanList {
    @Id
    private int banListId;
    private int userId;
    private int banUserId;
}
