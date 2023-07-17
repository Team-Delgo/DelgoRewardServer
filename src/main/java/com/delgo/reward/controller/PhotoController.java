package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photo")
public class PhotoController extends CommController {

    private final CertService certService;
    private final UserService userService;
    private final PhotoService photoService;
    private final MungpleService mungpleService;
    private final AchievementsService achievementsService;

    /*
     * profile 등록 및 수정 [회원가입 or AccountPage]
     * Request Data : userId, photo
     * - userId : NCP에 올릴 Photo File명에 사용됨. & DB에 photoUrl 등록할 때 User 식별
     * - photo : 확장자는 .png를 기본으로 한다. [.jpeg도 가능] [ 따로 확장자를 체크하진 않는다.]
     * Response Data : ApiCode
     */
    @PostMapping(value={"/upload/profile/{userId}","/upload/profile"})
    public ResponseEntity<?> uploadProfile(@PathVariable Integer userId, @RequestPart(required = false) MultipartFile photo) {
        if (photo.isEmpty()) ParamErrorReturn("photo");

        return SuccessReturn(userService.changePhoto(userId, photoService.uploadProfile(userId, photo)));
    }

    /*
     * 인증 사진 등록 및 수정
     * Request Data : certificationId, photo
     * - certificationId : NCP에 올릴 Photo File명에 사용됨. & DB에 photoUrl 등록할 때 User 식별
     * - photo : 확장자는 .png를 기본으로 한다. [.jpeg도 가능] [ 따로 확장자를 체크하진 않는다.]
     * Response Data : 사진 저장된 URL
     */
    @PostMapping(value={"/upload/certification/{certificationId}","/upload/certification"})
    public ResponseEntity<?> uploadCertificationPhoto(@PathVariable Integer certificationId, @RequestPart MultipartFile photo) {
        if (photo.isEmpty()) ParamErrorReturn("photo");

        return SuccessReturn(certService.changeCertPhotoUrl(
                certService.getCertById(certificationId),
                photoService.setCacheInvalidation(photoService.updateCertMultipart(certificationId, photo))));
    }

    /*
     * 멍플 사진 등록 및 수정
     * Request Data : mungpleId, photo
     * Response Data : 사진 저장된 URL
     */
    @PostMapping(value={"/mungple/{mungpleId}","/upload/mungple"})
    public ResponseEntity<?> uploadMungplePhoto(@PathVariable Integer mungpleId, @RequestPart MultipartFile photo) {
        if (photo.isEmpty()) ParamErrorReturn("photo");
        return SuccessReturn(mungpleService.modifyPhoto(mungpleId,photo));
    }

    /*
     * 업적 사진 등록
     * Request Data : achievementsId, photo
     * Response Data : 사진 저장된 URL
     */
    @PostMapping(value={"/achievements/{achievementsId}","/achievements"})
    public ResponseEntity<?> uploadAchievements(@PathVariable Integer achievementsId, @RequestPart(required = false) MultipartFile photo) {
        if (photo.isEmpty()) ParamErrorReturn("photo");

        Achievements achievements = achievementsService.getAchievements(achievementsId)
                .setImg(photoService.uploadAchievements(achievementsId, photo));

        log.info("achievements : {}", achievements);

        return SuccessReturn(achievementsService.save(achievements));
    }

    /*
     * photo file -> webp로 변환
     * Request Data : photo
     * Response Data : null
     */
//    @PutMapping("/webp")
//    public ResponseEntity<?> convertWebp(@RequestPart MultipartFile photo) {
//        photoService.convertWebp(photo);

//        return SuccessReturn();
//    }

    //    /*
//     * 멍플 노트 등록 [ Deprecated ]
//     * Request Data : mungpleId, photo
//     * Response Data : 사진 저장된 URL
//     */
//    @PostMapping(value={"/mungplenote/{mungpleId}","/upload/mungplenote"})
//    public ResponseEntity<?> uploadMungpleNote(@PathVariable Integer mungpleId, @RequestPart(required = false) MultipartFile photo) {
//        if (photo.isEmpty()) ParamErrorReturn("photo");
//
//        Mungple mungple = mungpleService.getMungpleById(mungpleId).setDetailUrl(photoService.uploadMungpleNote(photo));
//        return SuccessReturn(mungpleService.register(mungple));
//    }
}
