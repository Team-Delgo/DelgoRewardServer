package com.delgo.reward.service;


import com.delgo.reward.domain.certification.CertPhoto;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.certification.Reaction;
import com.delgo.reward.dto.cert.CertResponse;
import com.delgo.reward.dto.map.OtherMapDTO;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MapService {

    private final CertService certService;
    private final UserService userService;
    private final ReactionService reactionService;
    private final CertPhotoService certPhotoService;
    private final MongoMungpleRepository mongoMungpleRepository;

    public Map<String, Object> getMap(int userId) {
        List<MongoMungple > mungples = mongoMungpleRepository.findByIsActive(true);
        List<MungpleResDTO > mungpleResDTOS = mungples.stream().map(MungpleResDTO::new).toList();
        return  Map.of("mungpleList", mungpleResDTOS);
    }

    public OtherMapDTO getOtherMap(int userId) {
        List<Certification> certificationList = certService.getCorrectCertsByUserId(userId);
        Map<Integer,List<Reaction>> reactionMap = reactionService.getMapByCertList(certificationList);
        Map<Integer,List<CertPhoto>> photoMap = certPhotoService.getMapByCertList(certificationList);
        return new OtherMapDTO(
                userService.getUserById(userId),
                CertResponse.fromList(userId, certificationList, reactionMap, photoMap),  // 인증 리스트 조회
                certService.getCorrectCertCountByUserId(userId)
        );
    }
}
