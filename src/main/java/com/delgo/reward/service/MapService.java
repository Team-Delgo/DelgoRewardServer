package com.delgo.reward.service;


import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.domain.Reaction;
import com.delgo.reward.cert.response.CertResponse;
import com.delgo.reward.cert.service.ReactionService;
import com.delgo.reward.dto.map.OtherMapDTO;
import com.delgo.reward.dto.mungple.MungpleResponse;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import com.delgo.reward.mongoRepository.MungpleRepository;
import com.delgo.reward.cert.service.CertQueryService;
import com.delgo.reward.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class MapService {
    private final UserQueryService userQueryService;
    private final ReactionService reactionService;
    private final CertQueryService certQueryService;
    private final MungpleRepository mungpleRepository;

    public Map<String, Object> getMap() {
        List<Mungple> mungpleList = mungpleRepository.findListByIsActive(true);
        return  Map.of("mungpleList", MungpleResponse.fromList(mungpleList));
    }

    public OtherMapDTO getOtherMap(int userId) {
        Page<Certification> page = certQueryService.getCorrectPagingListByUserId(userId, Pageable.unpaged());
        Map<Integer,List<Reaction>> reactionMap = reactionService.getMapByCertList(page.getContent());
        return new OtherMapDTO(
                userQueryService.getOneByUserId(userId),
                CertResponse.fromList(userId, page.getContent(), reactionMap),  // 인증 리스트 조회
                page.getTotalElements()
        );
    }
}
