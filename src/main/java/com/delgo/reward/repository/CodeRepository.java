package com.delgo.reward.repository;

import com.delgo.reward.domain.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Integer>, JpaSpecificationExecutor<Code> {
    Optional<Code> findByCodeName(String codeName);

    Optional<Code> findByCode(String code);

    // 소문자 다음에 바로 대문자가 나올 경우 앞에 소문자로 써줘야 한다. ex findByPCode (x) findBypCode (o)
//    Optional<Code> findByCodeNameAndpCode(String codeName, String pCode);
    Optional<Code> findBypCodeAndCodeName(String pCode, String codeName);
}
