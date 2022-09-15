package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.domain.Certification;
import com.delgo.reward.service.CertificationService;
import com.delgo.reward.service.PhotoService;
import com.delgo.reward.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/photo")
public class PhotoController extends CommController {

    private final PhotoService photoService;
    private final CertificationService certificationService;

    /*
     * 인증 사진 등록 및 수정
     * Request Data : certificationId, photo
     * - certificationId : NCP에 올릴 Photo File명에 사용됨. & DB에 photoUrl 등록할 때 User 식별
     * - photo : 확장자는 .jpg를 기본으로 한다. [.jpeg도 가능] [ 따로 확장자를 체크하진 않는다.]
     * Response Data : 사진 저장된 URL
     */
    @PostMapping(value={"/upload/certification/{certificationId}","/upload/certification"})
    public ResponseEntity<?> uploadPetProfile(@PathVariable Integer certificationId, @RequestPart(required = false) MultipartFile photo) {
        if (photo.isEmpty()) // Validate - Empty Check;
            return ErrorReturn(ApiCode.PARAM_ERROR);

        String profileUrl = photoService.uploadCertificationPhoto(certificationId, photo);

        //NCP ERROR
        if (profileUrl.split(":")[0].equals("error")) {
            log.info("NCP ERROR : {}", profileUrl.split(":")[1]);
            return ErrorReturn(ApiCode.PHOTO_UPLOAD_ERROR);
        }

        Certification certification = certificationService.getCertificationByCertificationId(certificationId);
        certification.setPhotoUrl(profileUrl);

        // PhotoUrl 등록
        certificationService.registerCertification(certification);

        return SuccessReturn(profileUrl);
    }
}
