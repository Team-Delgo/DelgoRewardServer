package com.delgo.reward.service;


import com.delgo.reward.domain.certification.CertPhoto;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.certification.Reaction;
import com.delgo.reward.dto.cert.CertResponse;
import com.delgo.reward.dto.map.OtherMapDTO;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.service.cert.CertQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class MapService {

    private final CertQueryService certQueryService;
    private final UserService userService;
    private final ReactionService reactionService;
    private final CertPhotoService certPhotoService;
    private final MongoMungpleRepository mongoMungpleRepository;

    public Map<String, Object> getMap() {
        List<MongoMungple > mungples = mongoMungpleRepository.findByIsActive(true);
        List<MungpleResDTO > mungpleResDTOS = mungples.stream().map(MungpleResDTO::new).toList();
        return  Map.of("mungpleList", mungpleResDTOS);
    }

    public OtherMapDTO getOtherMap(int userId) {
        Page<Certification> page = certQueryService.getCorrectPagingListByUserId(userId, Pageable.unpaged());
        Map<Integer,List<Reaction>> reactionMap = reactionService.getMapByCertList(page.getContent());
        Map<Integer,List<CertPhoto>> photoMap = certPhotoService.getMapByCertList(page.getContent());
        return new OtherMapDTO(
                userService.getUserById(userId),
                CertResponse.fromList(userId, page.getContent(), reactionMap, photoMap),  // 인증 리스트 조회
                page.getTotalElements()
        );
    }
}
