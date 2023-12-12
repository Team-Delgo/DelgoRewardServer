package com.delgo.reward.repository;

import com.delgo.reward.comm.code.CodeType;
import com.delgo.reward.domain.code.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Integer>, JpaSpecificationExecutor<Code> {
    List<Code> findListByType(CodeType type);
    Optional<Code> findByCode(String code);
    Optional<Code> findByCodeName(String codeName);
    // 소문자 다음에 바로 대문자가 나올 경우 앞에 소문자로 써줘야 한다. ex findByPCode (x) findBypCode (o)
    Optional<Code> findBypCodeAndCodeName(String pCode, String codeName);
}
