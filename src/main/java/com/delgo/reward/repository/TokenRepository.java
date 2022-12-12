package com.delgo.reward.repository;

import com.delgo.reward.domain.user.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Integer> {
}
