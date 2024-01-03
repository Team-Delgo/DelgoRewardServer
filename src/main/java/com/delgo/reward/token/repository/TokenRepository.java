package com.delgo.reward.token.repository;

import com.delgo.reward.token.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Integer> {
}
