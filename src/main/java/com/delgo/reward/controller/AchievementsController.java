package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.domain.achievements.AchievementsCondition;
import com.delgo.reward.domain.achievements.Archive;
import com.delgo.reward.dto.achievements.AchievementsDTO;
import com.delgo.reward.dto.achievements.MainAchievementsDTO;
import com.delgo.reward.service.AchievementsConditionService;
import com.delgo.reward.service.AchievementsService;
import com.delgo.reward.service.ArchiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/achievements")
public class AchievementsController extends CommController {

    private final ArchiveService archiveService;
    private final AchievementsService achievementsService;
    private final AchievementsConditionService achievementsConditionService;

    /*
     * 업적 등록
     * Request Data :
     * Response Data :
     */
    @PostMapping
    public ResponseEntity register(@RequestBody AchievementsDTO dto) {

        String img;
        switch (dto.getCategoryCode()){
            case "CA0001": img = "https://kr.object.ncloudstorage.com/reward-achivements/%ED%98%95%ED%83%9C%3D%EC%B9%B4%EB%93%9C%2C%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%3D%EC%82%B0%EC%B1%85.png";break;
            case "CA0002": img = "https://kr.object.ncloudstorage.com/reward-achivements/%ED%98%95%ED%83%9C%3D%EC%B9%B4%EB%93%9C%2C%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%3D%EC%B9%B4%ED%8E%98.png";break;
            case "CA0003": img = "https://kr.object.ncloudstorage.com/reward-achivements/%ED%98%95%ED%83%9C%3D%EC%B9%B4%EB%93%9C%2C%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%3D%EC%8B%9D%EB%8B%B9.png";break;
            case "CA0004": img = "https://kr.object.ncloudstorage.com/reward-achivements/%ED%98%95%ED%83%9C%3D%EC%B9%B4%EB%93%9C%2C%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%3D%EB%AA%A9%EC%9A%95.png";break;
            case "CA0005": img = "https://kr.object.ncloudstorage.com/reward-achivements/%ED%98%95%ED%83%9C%3D%EC%B9%B4%EB%93%9C%2C%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%3D%EB%AF%B8%EC%9A%A9.png";break;
            case "CA0006": img = "https://kr.object.ncloudstorage.com/reward-achivements/%ED%98%95%ED%83%9C%3D%EC%B9%B4%EB%93%9C%2C%20%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC%3D%EB%B3%91%EC%9B%90.png";break;
            default: img = ""; break;
        }
        Achievements achievements = achievementsService.register(dto.toEntity(img));
        AchievementsCondition aCondition = achievementsConditionService.register(AchievementsCondition.builder()
                .achievementsId(achievements.getAchievementsId())
                .categoryCode(dto.getCategoryCode())
                .count(dto.getCount())
                .mungpleId(0)
                .build()
        );

        log.info("achievements : {}", achievements);
        log.info("aCondition : {}", aCondition);

        return SuccessReturn();
    }

    /*
     * 유저 획득 업적 리스트 조회
     * Request Data : userId
     * Response Data : 유저획득 업적 리스트
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity getUserData(@PathVariable Integer userId) {
        List<Achievements> achievements = achievementsService.getAchievementsAll();
        List<Archive> archives = archiveService.getArchiveByUserId(userId); // 유저 획득 업적

        for (Achievements achievement : achievements)
            for(Archive archive : archives)
                if(Objects.equals(archive.getAchievementsId(), achievement.getAchievementsId())) {
                    achievement.setIsActive(true);
                    achievement.setIsMain(archive.getIsMain());
                }

        String lockImg = "https://kr.object.ncloudstorage.com/reward-achivements/%EC%9E%A0%EA%B8%88%ED%99%94%EB%A9%B4.png";
        achievements.stream().filter(a-> !a.getIsActive()).forEach(a -> a.setImgUrl(lockImg));

        List<Achievements> sortedMainAchievements = achievements.stream().filter(a->a.getIsMain()!=0).sorted(Comparator.comparing(Achievements::getIsMain)).collect(Collectors.toList());
        List<Achievements> notMainAchievements = achievements.stream().filter(a->a.getIsMain()==0).collect(Collectors.toList());
        sortedMainAchievements.addAll(notMainAchievements);

        return SuccessReturn(sortedMainAchievements);
    }

    /*
     * 대표 업적 설정
     * Request Data : userId, archive
     * Response Data : 유저획득 업적 리스트
     */
    @PutMapping("/main")
    public ResponseEntity setMainAchievements(@RequestBody MainAchievementsDTO mainAchievementsDTO) {
        // 대표 업적 초기화
        archiveService.resetMainAchievements(mainAchievementsDTO.getUserId());

        // 사용자 지정 업적으로 대표업적 설정
        List<Archive> newMainAchievements = archiveService.setMainArchive(mainAchievementsDTO);
        for(Archive archive : newMainAchievements)
            archive.setAchievements(achievementsService.getAchievementsById(archive.getAchievementsId()));

        return SuccessReturn(newMainAchievements);
    }
}